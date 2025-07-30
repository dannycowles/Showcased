import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';

import {ShowService} from '../../../services/show.service';
import {ProfileService} from '../../../services/profile.service';
import {ToastDisplayService} from '../../../services/toast.service';
import {UtilsService} from '../../../services/utils.service';
import {ShowData} from '../../../data/show/show-data';
import {ShowReviewData} from '../../../data/reviews-data';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {AddReviewModalComponent} from '../../../components/add-review-modal/add-review-modal.component';
import {AddToCollectionModalComponent} from '../../../components/add-to-collection-modal/add-to-collection-modal.component';
import {AuthenticationService} from '../../../services/auth.service';
import {ReviewType} from '../../../data/enums';
import {AddShowReviewDto} from '../../../data/dto/add-review-dto';
import {AddToShowRankingList, AddToWatchingListDto, AddToWatchlistDto} from '../../../data/dto/add-to-list-dto';
import {PageData} from '../../../data/page-data';
import {SortReviewOption, sortReviewOptions} from '../../../data/constants';

@Component({
  selector: 'app-show-page',
  templateUrl: './show-page.component.html',
  styleUrl: './show-page.component.css',
  standalone: false
})
export class ShowPageComponent implements OnInit {
  showId: number;
  show: ShowData;
  reviews: PageData<ShowReviewData>;
  notifReviewId: number | null = null;
  notifCommentId: number | null = null;
  isLoggedIn: boolean = false;
  selectedSort: SortReviewOption = sortReviewOptions[0];

  constructor(private route: ActivatedRoute,
              private showService: ShowService,
              private profileService: ProfileService,
              private toastService: ToastDisplayService,
              public utilsService: UtilsService,
              private modalService: NgbModal,
              private authService: AuthenticationService,
              private router: Router) {
    this.route.params.subscribe(params => {
      this.showId = params['id'];
      this.notifReviewId = this.router.getCurrentNavigation()?.extras?.state?.['reviewId'];
      this.notifCommentId = this.router.getCurrentNavigation()?.extras?.state?.['commentId'];
      history.replaceState({}, document.title, window.location.href);
      this.loadShowData();
    });
  }

  async ngOnInit() {
    try {
      this.isLoggedIn = await this.authService.loginStatus();
    } catch (error) {
      console.error(error);
    }
  }

  async setSort(option: SortReviewOption) {
    if (this.selectedSort === option) return;
    this.selectedSort = option;

    try {
      this.reviews = await this.showService.fetchShowReviews(this.showId, 1, this.selectedSort.value);
    } catch (error) {
      console.error(error);
    }
  }

  async loadShowData() {
    // Retrieve show data from backend
    try {
      this.show = await this.showService.fetchShowDetails(this.showId);
    } catch (error) {
      console.error(error);
    }

    // Retrieve reviews for show from backend
    try {
      this.reviews = await this.showService.fetchShowReviews(this.showId);
    } catch (error) {
      console.error(error);
    }

    // If there was a notification review in the navigation state, fetch that review and append it to the beginning of the reviews list
    if (this.notifReviewId != null) {
      try {
        const notifReview = await this.showService.fetchShowReview(this.notifReviewId);

        // Filter out the review if it already exists on the first page of results
        this.reviews.content = this.reviews.content.filter(review => review.id != this.notifReviewId);

        // Appends the notification review to the beginning of the first page of results
        this.reviews.content.unshift(notifReview);

        if (this.notifCommentId == null) {
          // Scroll to the review itself
          requestAnimationFrame(() => {
            const reviewElement = document.getElementById(String(this.notifReviewId));
            if (reviewElement) {
              reviewElement.scrollIntoView({ behavior: 'smooth', block: 'start' });

              reviewElement.classList.add('highlight');
              setTimeout(() => reviewElement.classList.remove('highlight'), 2000);
            }
          });
        } else {
          // Retrieve the first page of comments, and then fetch the notification comment
          this.reviews.content[0].comments = await this.showService.getShowReviewComments(this.notifReviewId)
          const notifComment = await this.showService.getShowReviewComment(this.notifCommentId);

          // If the comment exists on the first page, filter it out before appending to beginning so no duplicates
          this.reviews.content[0].comments.content = this.reviews.content[0].comments.content.filter(comment => comment.id != notifComment.id);
          this.reviews.content[0].comments.content.unshift(notifComment);
          this.reviews.content[0] = {...this.reviews.content[0], notifCommentId: this.notifCommentId};

          requestAnimationFrame(() => {
            const commentElement = document.getElementById(String(this.notifCommentId));
            if (commentElement) {
              commentElement.scrollIntoView({ behavior: 'smooth', block: 'start' });
              commentElement.classList.add('highlight');
              setTimeout(() => commentElement.classList.remove('highlight'), 2000);
            }
          });
        }
      } catch (error) {
        console.error(error);
      }
    }
  }

  async openAddReviewModal() {
    if (!this.isLoggedIn) {
      this.router.navigate(['login']);
      return;
    }

    try {
      const addReviewModalRef =  this.modalService.open(AddReviewModalComponent, {
        ariaLabelledBy: "addReviewModal",
        centered: true
      });
      addReviewModalRef.componentInstance.modalTitle = `Add New Review for ${this.show.title}`;

      const result =  await addReviewModalRef.result;
      await this.reviewSubmitted(result);
    } catch (modalDismissReason) {}
  }

  async openAddToCollectionModal() {
    if (!this.isLoggedIn) {
      this.router.navigate(['login']);
      return;
    }

    try {
      const addToCollectionModalRef = this.modalService.open(AddToCollectionModalComponent, {
        ariaLabelledBy: "addToCollectionModal",
        centered: true
      });

      addToCollectionModalRef.componentInstance.show = {
        showId: this.showId,
        title: this.show.title,
        posterPath: this.show.posterPath
      };
    } catch (modalDismissReason) {}
  }

  async reviewSubmitted(data: any) {
    const reviewData: AddShowReviewDto = {
      rating: data.rating,
      showTitle: this.show.title,
      commentary: data.commentary,
      containsSpoilers: data.containsSpoilers,
      posterPath: this.show.posterPath
    };

    try {
      const response = await this.showService.addShowReview(this.showId, reviewData);

      if (response.ok) {
        const newReview: ShowReviewData = await response.json();
        this.reviews.content.unshift(newReview);
      }
    } catch(error) {
      console.error(error);
    }
  }

  async loadMoreReviews() {
    // If all reviews have been loaded, return
    if (this.reviews.page.number + 1 >= this.reviews.page.totalPages) {
      return;
    }

    try {
      const result = await this.showService.fetchShowReviews(this.showId, this.reviews.page.number + 2, this.selectedSort.value);
      if (this.notifReviewId !== null) {
        result.content = result.content.filter(review => review.id !== this.notifReviewId);
      }
      this.reviews.content.push(...result.content);
      this.reviews.page = result.page;
    } catch (error) {
      console.error(error);
    }
  }


  // Adds the current show to the user's watchlist
  async addShowToWatchlist() {
    if (this.isUserListConflict()) {
      this.toastService.addConflictToast(this.show.title);
      return;
    }

    try {
      const data: AddToWatchlistDto = {
        showId: this.showId,
        showTitle: this.show.title,
        posterPath: this.show.posterPath
      };
      const response = await this.profileService.addShowToWatchlist(data);

      if (response.status == 201) {
        // Display a toast that confirms the show was successfully added
        this.toastService.addToWatchlistToast(this.show.title);
        this.show.isOnWatchlist = true;
      }
    } catch (error) {
      console.error(error);
    }
  }

  async removeShowFromWatchlist() {
    try {
      await this.profileService.removeShowFromWatchlist(this.showId);
      this.show.isOnWatchlist = false;
    } catch(error) {
      console.error(error);
    }
  }

  // Adds the current show to the user's currently watching list
  async addShowToWatchingList() {
    if (this.isUserListConflict()) {
      this.toastService.addConflictToast(this.show.title);
      return;
    }

    try {
      const data: AddToWatchingListDto = {
        showId: this.showId,
        showTitle: this.show.title,
        posterPath: this.show.posterPath
      };
      const response = await this.profileService.addShowToWatchingList(data);

      if (response.status == 201) {
        // Display a toast that confirms the show was successfully added
        this.toastService.addToWatchingListToast(this.show.title);
        this.show.isOnWatchingList = true;
      }
    } catch (error) {
      console.error(error);
    }
  }

  async removeShowFromWatchingList() {
    try {
      await this.profileService.removeShowFromWatchingList(this.showId);
      this.show.isOnWatchingList = false;
    } catch(error) {
      console.error(error);
    }
  }

  // Adds the current show to the user's ranking list
  async addShowToRankingList() {
    if (this.isUserListConflict()) {
      this.toastService.addConflictToast(this.show.title);
      return;
    }

    try {
      const data: AddToShowRankingList = {
        showId: this.showId,
        showTitle: this.show.title,
        posterPath: this.show.posterPath
      };
      const response = await this.profileService.addShowToRankingList(data);

      if (response.status == 201) {
        // Display a toast that confirms the show was successfully added
        this.toastService.addToShowRankingToast(this.show.title);
        this.show.isOnRankingList = true;
      }
    } catch (error) {
      console.error(error);
    }
  }

  async removeShowFromRankingList() {
    try {
      await this.profileService.removeShowFromRankingList(this.showId);
      this.show.isOnRankingList = false;
    } catch(error) {
      console.error(error);
    }
  }

  // This method is called whenever a user attempts to add a show to any of their lists
  // If the user already has a show on ANY of their lists it cannot be added to another one
  // It wouldn't make sense for a show to be both on watchlist and currently watching for instance
  isUserListConflict(): boolean {
    return (this.show.isOnWatchingList || this.show.isOnWatchlist || this.show.isOnRankingList);
  }

  protected readonly ReviewType = ReviewType;
  protected readonly sortReviewOptions = sortReviewOptions;
}
