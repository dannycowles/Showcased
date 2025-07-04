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

@Component({
  selector: 'app-show-page',
  templateUrl: './show-page.component.html',
  styleUrl: './show-page.component.css',
  standalone: false
})
export class ShowPageComponent implements OnInit {
  showId: number;
  show: ShowData;
  reviews: ShowReviewData[];
  readonly heartSize: number = 100;
  isLoggedIn: boolean = false;

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
    const reviewData = {
      rating: data.rating,
      showTitle: this.show.title,
      commentary: data.commentary,
      containsSpoilers: data.containsSpoilers,
      posterPath: this.show.posterPath
    };

    try {
      await this.showService.addShowReview(this.showId, reviewData);
    } catch(error) {
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
  }


  // Adds the current show to the user's watchlist
  async addShowToWatchlist() {
    if (this.isUserListConflict()) {
      this.toastService.addConflictToast(this.show.title);
      return;
    }

    try {
      const data = {
        "showId": this.showId,
        "title": this.show.title,
        "posterPath": this.show
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
      let data = {
        "showId": this.showId,
        "title": this.show.title,
        "posterPath": this.show.posterPath
      };
      let response = await this.profileService.addShowToWatchingList(data);

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
      const data = {
        "showId": this.showId,
        "title": this.show.title,
        "posterPath": this.show.posterPath
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

  async toggleLikeState(review: ShowReviewData) {
    try {
      review.isLikedByUser = !review.isLikedByUser;
      if (review.isLikedByUser) {
        await this.showService.likeShowReview(review.id);
        review.numLikes++;
      } else {
        await this.showService.unlikeShowReview(review.id);
        review.numLikes--;
      }
    } catch (error) {
      console.error(error);
    }
  }

  // This method is called whenever a user attempts to add a show to any of their lists
  // If the user already has a show on ANY of their lists it cannot be added to another one
  // It wouldn't make sense for a show to be both on watchlist and currently watching for instance
  isUserListConflict(): boolean {
    return (this.show.isOnWatchingList || this.show.isOnWatchlist || this.show.isOnRankingList);
  }
}
