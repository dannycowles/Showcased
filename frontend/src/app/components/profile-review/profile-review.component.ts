import { Component, Input } from '@angular/core';
import { UtilsService } from '../../services/utils.service';
import { ButtonHeartComponent } from '../button-heart.component';
import { ShowService } from '../../services/show.service';
import { ReviewType } from '../../data/enums';
import {ProfileEpisodeReviewData, ProfileShowReviewData} from '../../data/profile-reviews-data';
import {ProfileReviewData} from '../../data/types';

@Component({
  selector: 'app-profile-review',
  imports: [ButtonHeartComponent],
  templateUrl: './profile-review.component.html',
  styleUrl: './profile-review.component.css',
  standalone: true,
})
export class ProfileReviewComponent {
  @Input({ required: true }) review: ProfileReviewData;

  constructor(
    public utilsService: UtilsService,
    private showService: ShowService,
  ) {}

  readonly likeHandlers = {
    [ReviewType.Show]: {
      like: (id: number) => this.showService.likeShowReview(id),
      unlike: (id: number) => this.showService.unlikeShowReview(id),
    },
    [ReviewType.Episode]: {
      like: (id: number) => this.showService.likeEpisodeReview(id),
      unlike: (id: number) => this.showService.unlikeEpisodeReview(id),
    },
  };

  async toggleLikeState() {
    try {
      const handler = this.likeHandlers[this.review.type];

      if (!this.review.isLikedByUser) {
        await handler.like(this.review.id);
        this.review.isLikedByUser = true;
        this.review.numLikes++;
      } else {
        await handler.unlike(this.review.id);
        this.review.isLikedByUser = false;
        this.review.numLikes--;
      }
    } catch (error) {
      console.error(error);
    }
  }

  isShowReview(review: ProfileReviewData): review is ProfileShowReviewData {
    return review.type === ReviewType.Show;
  }

  isEpisodeReview(review: ProfileReviewData): review is ProfileEpisodeReviewData {
    return review.type === ReviewType.Episode;
  }
}
