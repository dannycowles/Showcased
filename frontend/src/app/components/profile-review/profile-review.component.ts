import {booleanAttribute, Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import { UtilsService } from '../../services/utils.service';
import { ButtonHeartComponent } from '../button-heart.component';
import { ShowService } from '../../services/show.service';
import { ReviewType } from '../../data/enums';
import {ProfileEpisodeReviewData, ProfileShowReviewData} from '../../data/profile-reviews-data';
import {ProfileReviewData} from '../../data/types';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {EditReviewModalComponent} from '../edit-review-modal/edit-review-modal.component';
import {UpdateReviewDto} from '../../data/dto/update-review-dto';

@Component({
  selector: 'app-profile-review',
  imports: [ButtonHeartComponent],
  templateUrl: './profile-review.component.html',
  styleUrl: './profile-review.component.css',
  standalone: true,
})
export class ProfileReviewComponent implements OnInit {
  @Input({ required: true }) review: ProfileReviewData;
  @Input({ transform: booleanAttribute }) editable: boolean = false;
  @Output() delete = new EventEmitter<ProfileReviewData>;
  reviewHandler: any = null;

  constructor(public utilsService: UtilsService,
              private showService: ShowService,
              private modalService: NgbModal) {};

  ngOnInit() {
    this.reviewHandler = this.reviewMethods[this.review.type];
  }

  readonly reviewMethods = {
    [ReviewType.Show]: {
      like: () => this.showService.likeShowReview(this.review.id),
      unlike: () => this.showService.unlikeShowReview(this.review.id),
      delete: () => this.showService.deleteShowReview(this.review.id),
      update: (updates: UpdateReviewDto) => this.showService.updateShowReview(this.review.id, updates)
    },
    [ReviewType.Episode]: {
      like: () => this.showService.likeEpisodeReview(this.review.id),
      unlike: () => this.showService.unlikeEpisodeReview(this.review.id),
      delete: () => this.showService.deleteEpisodeReview(this.review.id),
      update: (updates: UpdateReviewDto) => this.showService.updateEpisodeReview(this.review.id, updates)
    },
  };

  async toggleLikeState() {
    try {
      if (!this.review.isLikedByUser) {
        await this.reviewHandler.like();
        this.review.isLikedByUser = true;
        this.review.numLikes++;
      } else {
        await this.reviewHandler.unlike();
        this.review.isLikedByUser = false;
        this.review.numLikes--;
      }
    } catch (error) {
      console.error(error);
    }
  }

  async deleteReview() {
    try {
      const response = await this.reviewHandler.delete();

      if (response.ok) {
        // Raise event to parent component so it can remove the review from the shown entries
        this.delete.emit(this.review);
      }
    } catch (error) {
      console.error(error);
    }
  }

  async openEditModal() {
    const editModalRef = this.modalService.open(EditReviewModalComponent, {
      ariaLabelledBy: "editReviewModal",
      centered: true
    });
    editModalRef.componentInstance.review = this.review;

    try {
      const modifiedReview: ProfileReviewData = await editModalRef.result;
      Object.assign(this.review, modifiedReview);

      const updateDto: UpdateReviewDto = {
        rating: modifiedReview.rating,
        commentary: modifiedReview.commentary,
        containsSpoilers: modifiedReview.containsSpoilers
      }
      this.reviewHandler.update(updateDto);
    } catch {

    }
  }

  isShowReview(review: ProfileReviewData): review is ProfileShowReviewData {
    return review.type === ReviewType.Show;
  }

  isEpisodeReview(review: ProfileReviewData): review is ProfileEpisodeReviewData {
    return review.type === ReviewType.Episode;
  }
}
