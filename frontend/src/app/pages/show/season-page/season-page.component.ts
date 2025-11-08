import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router, RouterLink} from '@angular/router';
import {SeasonData} from '../../../data/show/season-data';
import {ShowService} from '../../../services/show.service';
import {UtilsService} from '../../../services/utils.service';
import {ProfileService} from '../../../services/profile.service';
import {AddToSeasonRankingList} from '../../../data/dto/add-to-list-dto';
import {Title} from '@angular/platform-browser';
import {NgOptimizedImage} from '@angular/common';
import {NgbDropdown, NgbDropdownItem, NgbDropdownMenu, NgbDropdownToggle, NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {AddReviewModalComponent} from '../../../components/add-review-modal/add-review-modal.component';
import {AuthenticationService} from '../../../services/auth.service';
import {AddSeasonReviewDto, AddShowReviewDto} from '../../../data/dto/add-review-dto';
import {SeasonReviewData, ShowReviewData} from '../../../data/reviews-data';
import {PageData} from '../../../data/page-data';
import {InfiniteScrollDirective} from 'ngx-infinite-scroll';
import {ReviewComponent} from '../../../components/review/review.component';
import {ReviewType} from '../../../data/enums';
import {SortReviewOption, sortReviewOptions} from '../../../data/constants';


@Component({
  selector: 'app-season-page',
  templateUrl: './season-page.component.html',
  styleUrl: './season-page.component.css',
  imports: [
    RouterLink,
    NgOptimizedImage,
    NgbDropdown,
    NgbDropdownMenu,
    NgbDropdownItem,
    NgbDropdownToggle,
    InfiniteScrollDirective,
    ReviewComponent,
  ],
  standalone: true,
})
export class SeasonPageComponent implements OnInit {
  readonly showId: number;
  seasonNumber: number;
  season: SeasonData;
  numSeasons: number;
  isLoggedIn: boolean = false;
  reviews: PageData<SeasonReviewData>;
  selectedSort: SortReviewOption = sortReviewOptions[0];
  notifReviewId: number | null = null;
  notifCommentId: number | null = null;

  constructor(
    private route: ActivatedRoute,
    private showService: ShowService,
    public utilsService: UtilsService,
    private profileService: ProfileService,
    private authService: AuthenticationService,
    private modalService: NgbModal,
    private router: Router,
    private title: Title,
  ) {
    this.showId = this.route.snapshot.params['id'];
    this.route.params.subscribe((params) => {
      this.seasonNumber = params['seasonNumber'];
      this.retrieveSeasonInfo();
    });
  }

  async ngOnInit() {
    this.isLoggedIn = this.authService.isLoggedIn();

    // Retrieve number of seasons from backend
    try {
      this.numSeasons = await this.showService.fetchNumberOfSeasons(this.showId);
    } catch (error) {
      console.error(error);
    }
  }

  async retrieveSeasonInfo() {
    // Retrieve season details from backend
    try {
      this.season = await this.showService.fetchSeasonDetails(this.showId, this.seasonNumber);
      this.title.setTitle(`${this.season.showTitle} S${this.seasonNumber} | Showcased`);
    } catch (error) {
      console.error(error);
    }

    // Retrieve season reviews from backend
    try {
      this.reviews = await this.showService.getSeasonReviews(this.season.id);
    } catch (error) {
      console.error(error);
    }
  }

  async setSort(option: SortReviewOption) {
    if (this.selectedSort === option) return;
    this.selectedSort = option;

    try {
      this.reviews = await this.showService.getSeasonReviews(this.showId, 1, this.selectedSort.value);
    } catch (error) {
      console.error(error);
    }
  }

  async loadMoreReviews() {
    // If all reviews have been loaded, return
    if (this.reviews.page.number + 1 >= this.reviews.page.totalPages) {
      return;
    }

    try {
      const result = await this.showService.getSeasonReviews(this.season.id, this.reviews.page.number + 2, this.selectedSort.value);
      if (this.notifReviewId !== null) {
        result.content = result.content.filter((review) => review.id !== this.notifReviewId);
      }
      this.reviews.content.push(...result.content);
      this.reviews.page = result.page;
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
      const addReviewModalRef = this.modalService.open(AddReviewModalComponent, {
        ariaLabelledBy: 'addReviewModal',
        centered: true
      });
      addReviewModalRef.componentInstance.modalTitle = `Add Review for ${this.season.showTitle} S${this.season.seasonNumber}`;

      const result = await addReviewModalRef.result;
      await this.reviewSubmitted(result);
    } catch (modalDismissReason) {}
  }

  async reviewSubmitted(data: any) {
    const reviewData: AddSeasonReviewDto = {
      rating: data.rating,
      showTitle: this.season.showTitle,
      showId: this.showId,
      season: this.season.seasonNumber,
      commentary: data.commentary,
      containsSpoilers: data.containsSpoilers,
      posterPath: this.season.posterPath,
    };

    try {
      const response = await this.showService.addSeasonReview(this.season.id, reviewData);

      if (response.ok) {
        const newReview: SeasonReviewData = await response.json();
        this.reviews.content.unshift(newReview);
      }
    } catch (error) {
      console.error(error);
    }
  }

  async addSeasonToRankingList() {
    try {
      const data: AddToSeasonRankingList = {
        showId: this.showId,
        season: this.seasonNumber,
        posterPath: this.season.posterPath,
        showTitle: this.season.showTitle,
      };

      const response = await this.profileService.addSeasonToRankingList(data);
      if (response.ok) {
        this.season.onRankingList = true;
      }
    } catch (error) {
      console.error(error);
    }
  }

  async removeSeasonFromRankingList() {
    try {
      const response = await this.profileService.removeSeasonFromRankingList(this.season.id);
      if (response.ok) {
        this.season.onRankingList = false;
      }
    } catch (error) {
      console.error(error);
    }
  }

  async handleDeleteReview(deleteId: number) {
    try {
      const response = await this.showService.deleteSeasonReview(deleteId);
      if (response.ok) {
        this.reviews.content = this.reviews.content.filter((review) => review.id !== deleteId);
      }
    } catch (error) {
      console.error(error);
    }
  }

  protected readonly ReviewType = ReviewType;
  protected readonly sortReviewOptions = sortReviewOptions;
}
