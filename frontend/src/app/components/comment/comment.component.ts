import {Component, Input} from '@angular/core';
import {NgOptimizedImage} from '@angular/common';
import {RouterLink} from '@angular/router';
import {ButtonHeartComponent} from '../button-heart.component';
import {UtilsService} from '../../services/utils.service';
import {ShowService} from '../../services/show.service';
import {ReviewCommentData} from '../../data/review-comment-data';
import {ReviewType} from '../../data/enums';

@Component({
  selector: 'app-comment',
  imports: [NgOptimizedImage, RouterLink, ButtonHeartComponent],
  templateUrl: './comment.component.html',
  styleUrl: './comment.component.css',
  standalone: true,
})
export class CommentComponent {
  @Input({ required: true }) comment: ReviewCommentData;
  @Input({ required: true }) reviewType: ReviewType;
  readonly heartSize = 100;

  constructor(public utilsService: UtilsService,
              private showService: ShowService) {};

  readonly likeHandlers = {
    [ReviewType.Show]: {
      like: () => this.showService.likeShowReviewComment(this.comment.id),
      unlike: () => this.showService.unlikeShowReviewComment(this.comment.id)
    },
    [ReviewType.Episode]: {
      like: () => this.showService.likeEpisodeReviewComment(this.comment.id),
      unlike: () => this.showService.unlikeEpisodeReviewComment(this.comment.id)
    }
  };

  async toggleCommentLikeState() {
    try {
      const handler = this.likeHandlers[this.reviewType];

      if (!this.comment.isLikedByUser) {
        await handler.like();
        this.comment.isLikedByUser = true;
        this.comment.numLikes++;
      } else {
        await handler.unlike();
        this.comment.isLikedByUser = false;
        this.comment.numLikes--;
      }
    } catch (error) {
      console.error(error);
    }
  }
}
