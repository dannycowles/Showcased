import { Component } from '@angular/core';
import {PageData} from '../../../data/page-data';
import {ProfileReviewData} from '../../../data/types';
import {ReviewTypeOption, reviewTypeOptions, SortReviewOption, sortReviewOptions} from '../../../data/constants';
import {UserService} from '../../../services/user.service';
import {ActivatedRoute} from '@angular/router';

@Component({
  selector: 'app-user-reviews-page',
  templateUrl: './user-reviews-page.component.html',
  styleUrl: './user-reviews-page.component.css',
  standalone: false
})
export class UserReviewsPageComponent {
  readonly username: string;
  reviews: PageData<ProfileReviewData>;
  selectedReviewType: ReviewTypeOption = reviewTypeOptions[0];
  selectedSortOption: SortReviewOption = sortReviewOptions[0];
  isLoadingReviews: boolean = false;

  constructor(private userService: UserService,
              private route: ActivatedRoute) {
    this.username = this.route.snapshot.params['username'];
  };

  async ngOnInit() {
    await this.loadReviews();
  }

  async setReviewType(reviewType: ReviewTypeOption) {
    if (reviewType === this.selectedReviewType) return;
    this.selectedReviewType = reviewType;
    await this.loadReviews();
  }

  async setSortOption(sortOption: SortReviewOption) {
    if (sortOption === this.selectedSortOption) return;
    this.selectedSortOption = sortOption;
    await this.loadReviews();
  }

  async loadReviews() {
    try {
      this.isLoadingReviews = true;
      switch (this.selectedReviewType.value) {
        case 'all':
          this.reviews = await this.userService.getCombinedReviews(this.username,1, this.selectedSortOption.value);
          break;
        case 'show':
          this.reviews = await this.userService.getShowReviews(this.username, 1, this.selectedSortOption.value);
          break;
        case 'episode':
          this.reviews = await this.userService.getEpisodeReviews(this.username, 1, this.selectedSortOption.value);
          break;
      }
    } catch (error) {
      console.error(error);
    } finally {
      this.isLoadingReviews = false;
    }
  }

  async loadMoreReviews() {
    if (this.reviews.page.number + 1 >= this.reviews.page.totalPages) return;

    try {
      switch (this.selectedReviewType.value) {
        case 'all':
          this.reviews = await this.userService.getCombinedReviews(this.username, this.reviews.page.number + 2, this.selectedSortOption.value);
          break;
        case 'show':
          this.reviews = await this.userService.getShowReviews(this.username, this.reviews.page.number + 2, this.selectedSortOption.value);
          break;
        case 'episode':
          this.reviews = await this.userService.getEpisodeReviews(this.username, this.reviews.page.number + 2, this.selectedSortOption.value);
          break;
      }
    } catch (error) {
      console.error(error);
    }
  }

  protected readonly sortReviewOptions = sortReviewOptions;
  protected readonly reviewTypeOptions = reviewTypeOptions;
}
