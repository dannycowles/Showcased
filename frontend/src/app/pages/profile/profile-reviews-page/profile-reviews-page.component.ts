import {Component, OnInit} from '@angular/core';
import {ReviewTypeOption, reviewTypeOptions, SortReviewOption, sortReviewOptions} from '../../../data/constants';
import {ProfileService} from '../../../services/profile.service';
import {PageData} from '../../../data/page-data';
import {ProfileReviewData} from '../../../data/types';
import {RouterLink} from '@angular/router';
import {InfiniteScrollDirective} from 'ngx-infinite-scroll';
import {ProfileReviewComponent} from '../../../components/profile-review/profile-review.component';

@Component({
  selector: 'app-profile-reviews-page',
  templateUrl: './profile-reviews-page.component.html',
  styleUrl: './profile-reviews-page.component.css',
  imports: [RouterLink, InfiniteScrollDirective, ProfileReviewComponent],
  standalone: true,
})
export class ProfileReviewsPageComponent implements OnInit {
  reviews: PageData<ProfileReviewData>;
  selectedReviewType: ReviewTypeOption = reviewTypeOptions[0];
  selectedSortOption: SortReviewOption = sortReviewOptions[0];
  isLoadingReviews: boolean = false;

  constructor(private profileService: ProfileService) {}

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
          this.reviews = await this.profileService.getCombinedReviews(
            1,
            this.selectedSortOption.value,
          );
          break;
        case 'show':
          this.reviews = await this.profileService.getShowReviews(
            1,
            this.selectedSortOption.value,
          );
          break;
        case 'episode':
          this.reviews = await this.profileService.getEpisodeReviews(
            1,
            this.selectedSortOption.value,
          );
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
          this.reviews = await this.profileService.getCombinedReviews(
            this.reviews.page.number + 2,
            this.selectedSortOption.value,
          );
          break;
        case 'show':
          this.reviews = await this.profileService.getShowReviews(
            this.reviews.page.number + 2,
            this.selectedSortOption.value,
          );
          break;
        case 'episode':
          this.reviews = await this.profileService.getEpisodeReviews(
            this.reviews.page.number + 2,
            this.selectedSortOption.value,
          );
          break;
      }
    } catch (error) {
      console.error(error);
    }
  }

  deleteReview(deleteItem: ProfileReviewData) {
    this.reviews.content = this.reviews.content.filter(
      (review) => review !== deleteItem,
    );
  }

  protected readonly sortReviewOptions = sortReviewOptions;
  protected readonly reviewTypeOptions = reviewTypeOptions;
}
