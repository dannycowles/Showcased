import { Component, Input } from '@angular/core';
import { UtilsService } from '../../services/utils.service';
import { ButtonHeartComponent } from '../button-heart.component';
import { ShowService } from '../../services/show.service';
import { EpisodeReviewData, ShowReviewData } from '../../data/reviews-data';
import { ReviewType } from '../../data/enums';

@Component({
  selector: 'app-profile-review',
  imports: [ButtonHeartComponent],
  templateUrl: './profile-review.component.html',
  styleUrl: './profile-review.component.css',
  standalone: true,
})
export class ProfileReviewComponent {
  @Input({ required: true }) review: ShowReviewData | EpisodeReviewData;
  @Input({ required: true }) reviewType: ReviewType;

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
  readonly ReviewType = ReviewType;

  async toggleLikeState() {
    try {
      const handler = this.likeHandlers[this.reviewType];

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

  get episodeTitle(): string {
    const episodeReview = this.review as EpisodeReviewData
    return `S${episodeReview.season} E${episodeReview.episode}: ${episodeReview.episodeTitle}`;
  }
}
