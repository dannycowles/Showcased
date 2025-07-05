import {Component, Input} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {NgOptimizedImage} from '@angular/common';
import {EpisodeReviewData, ShowReviewData} from '../../data/reviews-data';
import {UtilsService} from '../../services/utils.service';
import {ButtonHeartComponent} from '../button-heart.component';
import {ShowService} from '../../services/show.service';
import {ReviewType} from '../../data/enums';
import {RouterLink} from '@angular/router';

@Component({
  selector: 'app-review',
  imports: [FormsModule, NgOptimizedImage, ButtonHeartComponent, RouterLink],
  templateUrl: './review.component.html',
  styleUrl: './review.component.css',
  standalone: true,
})
export class ReviewComponent {
  @Input({ required: true }) review: EpisodeReviewData | ShowReviewData;
  @Input({ required: true }) reviewType: ReviewType;
  readonly heartSize = 100;

  constructor(public utilsService: UtilsService,
              private showService: ShowService) {};

  readonly likeHandlers = {
    [ReviewType.Show]: {
      like: (id: number) => this.showService.likeShowReview(id),
      unlike: (id: number) => this.showService.unlikeShowReview(id)
    },
    [ReviewType.Episode]: {
      like: (id: number) => this.showService.likeEpisodeReview(id),
      unlike: (id: number) => this.showService.unlikeEpisodeReview(id)
    }
  };

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
}
