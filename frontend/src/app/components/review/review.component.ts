import {Component, Input} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {NgOptimizedImage} from '@angular/common';
import {EpisodeReviewData, ShowReviewData} from '../../data/reviews-data';
import {UtilsService} from '../../services/utils.service';
import {ButtonHeartComponent} from '../button-heart.component';
import {ShowService} from '../../services/show.service';
import {ReviewType} from '../../data/enums';
import {RouterLink} from '@angular/router';
import {CommentComponent} from '../comment/comment.component';

@Component({
  selector: 'app-review',
  imports: [
    FormsModule,
    NgOptimizedImage,
    ButtonHeartComponent,
    RouterLink,
    CommentComponent,
  ],
  templateUrl: './review.component.html',
  styleUrl: './review.component.css',
  standalone: true,
})
export class ReviewComponent {
  @Input({ required: true }) review: EpisodeReviewData | ShowReviewData;
  @Input({ required: true }) reviewType: ReviewType;
  readonly heartSize = 100;
  commentText: string = "";
  showCommentBox: boolean = false;

  readonly maxCommentLength = 1000;
  commentMessage: string = "";

  constructor(
    public utilsService: UtilsService,
    private showService: ShowService,
  ) {}

  readonly reviewHandlers = {
    [ReviewType.Show]: {
      like: () => this.showService.likeShowReview(this.review.id),
      unlike: () => this.showService.unlikeShowReview(this.review.id),
      getComments: () => this.showService.getShowReviewComments(this.review.id),
    },
    [ReviewType.Episode]: {
      like: () => this.showService.likeEpisodeReview(this.review.id),
      unlike: () => this.showService.unlikeEpisodeReview(this.review.id),
      getComments: () =>
        this.showService.getEpisodeReviewComments(this.review.id),
    },
  };

  async toggleReviewLikeState() {
    try {
      const handler = this.reviewHandlers[this.reviewType];

      if (!this.review.isLikedByUser) {
        await handler.like();
        this.review.isLikedByUser = true;
        this.review.numLikes++;
      } else {
        await handler.unlike();
        this.review.isLikedByUser = false;
        this.review.numLikes--;
      }
    } catch (error) {
      console.error(error);
    }
  }

  async getReviewComments() {
    try {
      const handler = this.reviewHandlers[this.reviewType];
      this.review.comments = await handler.getComments();
    } catch (error) {
      console.error(error);
    }
  }

  resetCommentBox() {
    this.showCommentBox = !this.showCommentBox;
    this.commentText = "";
  }

  async addComment() {
    try {
      const data = {
        commentText: this.commentText
      };
      const newComment = await this.showService.addCommentToShowReview(this.review.id, data);

      // If the comments are already loaded from the backend, push the newComment
      if (this.review.comments) {
        this.review.comments.push(newComment);
      }

      this.review.numComments++;
      this.showCommentBox = false;
      this.commentMessage = "Successfully added comment!";
    } catch (error) {
      console.error(error);
    } finally {
      setTimeout(() => this.commentMessage = "", 3000);
    }
  }
}
