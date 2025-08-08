import {Component, Input} from '@angular/core';
import {NgOptimizedImage} from '@angular/common';
import {RouterLink} from '@angular/router';
import {ButtonHeartComponent} from '../button-heart.component';
import {UtilsService} from '../../services/utils.service';
import {ShowService} from '../../services/show.service';
import {ReviewCommentData} from '../../data/review-comment-data';
import {ReviewType} from '../../data/enums';
import {FormsModule} from '@angular/forms';
import {AddCommentDto} from '../../data/dto/add-comment-dto';

@Component({
  selector: 'app-comment',
  imports: [NgOptimizedImage, RouterLink, ButtonHeartComponent, FormsModule],
  templateUrl: './comment.component.html',
  styleUrl: './comment.component.css',
  standalone: true,
})
export class CommentComponent {
  @Input({ required: true }) comment: ReviewCommentData;
  @Input({ required: true }) reviewType: ReviewType;
  readonly heartSize = 100;
  isEditing: boolean = false;
  editText: string = '';
  readonly maxCommentLength = 1000;

  constructor(
    public utilsService: UtilsService,
    private showService: ShowService,
  ) {}

  readonly likeHandlers = {
    [ReviewType.Show]: {
      like: () => this.showService.likeShowReviewComment(this.comment.id),
      unlike: () => this.showService.unlikeShowReviewComment(this.comment.id),
      update: (updates: AddCommentDto) => this.showService.updateShowReviewComment(this.comment.id, updates)
    },
    [ReviewType.Episode]: {
      like: () => this.showService.likeEpisodeReviewComment(this.comment.id),
      unlike: () => this.showService.unlikeEpisodeReviewComment(this.comment.id),
      update: (updates: AddCommentDto) => this.showService.updateEpisodeReviewComment(this.comment.id, updates)
    },
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

  isCommentEditable() {
    // Comments are only editable within 5 minutes of being written to avoid abuse
    const commentDate = new Date(this.comment.createdAt);
    const editableUntil = new Date(commentDate.getTime() + (5 * 60 * 1000));
    return new Date() <= editableUntil;
  }

  openEditBox() {
    this.editText = this.comment.commentText;
    this.isEditing = true;
  }

  async saveEdit() {
    this.comment.commentText = this.editText;
    const handler = this.likeHandlers[this.reviewType];

    const updates: AddCommentDto = {
      commentText: this.editText
    }

    try {
      const response = await handler.update(updates);

      if (response.ok) {
        this.isEditing = false;
      }
    } catch (error) {
      console.error(error);
    }
  }
}
