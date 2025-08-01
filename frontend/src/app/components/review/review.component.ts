import {Component, ElementRef, Input, OnChanges, SimpleChanges, ViewChild} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {NgOptimizedImage} from '@angular/common';
import {EpisodeReviewData, ShowReviewData} from '../../data/reviews-data';
import {UtilsService} from '../../services/utils.service';
import {ButtonHeartComponent} from '../button-heart.component';
import {ShowService} from '../../services/show.service';
import {ReviewType} from '../../data/enums';
import {Router, RouterLink} from '@angular/router';
import {CommentComponent} from '../comment/comment.component';
import {AddCommentDto} from '../../data/dto/add-comment-dto';

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
export class ReviewComponent implements OnChanges {
  @ViewChild("commentBox") commentBoxRef: ElementRef<HTMLTextAreaElement>;
  @Input({ required: true }) review: EpisodeReviewData | ShowReviewData;
  @Input({ required: true }) reviewType: ReviewType;
  readonly heartSize = 100;
  commentText: string = "";
  showCommentBox: boolean = false;
  isLoadingComments: boolean = false;
  areCommentsHidden: boolean = true;
  @Input({ required: true }) isLoggedIn: boolean = false;

  readonly maxCommentLength = 1000;
  commentMessage: string = "";

  constructor(public utilsService: UtilsService,
              private showService: ShowService,
              private router: Router) { };

  ngOnChanges() {
    if (this.review.notifCommentId != null) {
      this.areCommentsHidden = false;
    }
  }

  readonly reviewHandlers = {
    [ReviewType.Show]: {
      like: () => this.showService.likeShowReview(this.review.id),
      unlike: () => this.showService.unlikeShowReview(this.review.id),
      getComments: (page: number) => this.showService.getShowReviewComments(this.review.id, page),
      addComment: (comment: AddCommentDto) => this.showService.addCommentToShowReview(this.review.id, comment)
    },
    [ReviewType.Episode]: {
      like: () => this.showService.likeEpisodeReview(this.review.id),
      unlike: () => this.showService.unlikeEpisodeReview(this.review.id),
      getComments: (page: number) => this.showService.getEpisodeReviewComments(this.review.id, page),
      addComment: (comment: AddCommentDto) => this.showService.addCommentToEpisodeReview(this.review.id, comment)
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
    // If we have already retrieved the comments, use those to avoid another backend call
    if (this.review?.comments) {
      this.areCommentsHidden = false;
      return;
    }

    try {
      this.isLoadingComments = true;
      const handler = this.reviewHandlers[this.reviewType];
      this.review.comments = await handler.getComments(1);
    } catch (error) {
      console.error(error);
    } finally {
      this.areCommentsHidden = false;
      this.isLoadingComments = false;
    }
  }

  resetCommentBox() {
    if (!this.isLoggedIn) {
      this.router.navigate(['/login']);
      return;
    }

    this.showCommentBox = !this.showCommentBox;
    this.commentText = "";

    if (this.showCommentBox) {
      setTimeout(() => {
        this.commentBoxRef.nativeElement.focus();
        this.commentBoxRef.nativeElement.scrollIntoView();
      });
    }
  }

  async addComment() {
    try {
      const data: AddCommentDto = {
        commentText: this.commentText
      };
      const handler = this.reviewHandlers[this.reviewType];
      const newComment = await handler.addComment(data);

      // If the comments are already loaded from the backend, push the newComment
      if (this.review.comments) {
        this.review.comments.content.push(newComment);
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

  async getMoreReviewComments() {
    try {
      const handler = this.reviewHandlers[this.reviewType];
      const result = await handler.getComments(this.review.comments.page.number + 2);

      // Filter out comment if we are coming from a comment notification click
      if (this.review.notifCommentId != null) {
        result.content = result.content.filter(comment => comment.id != this.review.notifCommentId);
      }

      this.review.comments.content.push(...result.content);
      this.review.comments.page = result.page;
    } catch (error) {
      console.error(error);
    }
  }
}
