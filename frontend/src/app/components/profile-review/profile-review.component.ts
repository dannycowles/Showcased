import {booleanAttribute, Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {UtilsService} from '../../services/utils.service';
import {ButtonHeartComponent} from '../button-heart.component';
import {ShowService} from '../../services/show.service';
import {ReviewType} from '../../data/enums';
import {ProfileReviewData} from '../../data/types';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {EditReviewModalComponent} from '../edit-review-modal/edit-review-modal.component';
import {UpdateReviewDto} from '../../data/dto/update-review-dto';
import {Router} from '@angular/router';
import {ConfirmationService} from '../../services/confirmation.service';
import {NgOptimizedImage} from '@angular/common';

@Component({
  selector: 'app-profile-review',
  imports: [ButtonHeartComponent, NgOptimizedImage],
  templateUrl: './profile-review.component.html',
  styleUrl: './profile-review.component.css',
  standalone: true,
})
export class ProfileReviewComponent implements OnInit {
  @Input({ required: true }) review: ProfileReviewData;
  @Input({ transform: booleanAttribute }) editable: boolean = false;
  @Output() delete = new EventEmitter<ProfileReviewData>;
  reviewHandler: any = null;

  showSpoilers: boolean = false;

  constructor(public utilsService: UtilsService,
              private showService: ShowService,
              private modalService: NgbModal,
              private router: Router,
              private confirmationService: ConfirmationService) {};

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
    let itemName;
    switch (this.review.type) {
      case ReviewType.Show:
        itemName = this.review.showTitle;
        break;
      case ReviewType.Episode:
        itemName = `${this.review.showTitle} S${this.review.season} E${this.review.episode}`;
        break;
    }

    const confirmation = await this.confirmationService.confirmDeleteReview(itemName);
    if (confirmation) {
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

  navigateToReview() {
    switch(this.review.type) {
      case ReviewType.Show:
        this.router.navigate(['/show', this.review.showId], {
          state: {
            reviewId: this.review.id
          }
        });
        break;
      case ReviewType.Episode:
        this.router.navigate(['/show', this.review.showId, 'season', this.review.season, 'episode', this.review.episode], {
          state: {
            reviewId: this.review.id
          }
        });
        break;
    }
  }

  protected readonly ReviewType = ReviewType;
}
