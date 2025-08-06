import {Component, Input, OnInit} from '@angular/core';
import {NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';
import {FormsModule} from '@angular/forms';
import {ProfileReviewData} from '../../data/types';
import {ReviewType} from '../../data/enums';
import {ProfileEpisodeReviewData, ProfileShowReviewData} from '../../data/profile-reviews-data';

@Component({
  selector: 'app-edit-review-modal',
  imports: [FormsModule],
  templateUrl: './edit-review-modal.component.html',
  styleUrl: './edit-review-modal.component.css',
  standalone: true,
})
export class EditReviewModalComponent implements OnInit {
  @Input({ required: true }) review: ProfileReviewData;
  modifiedReview: ProfileReviewData;
  readonly maxCommentaryLength = 5000;

  constructor(public activeModal: NgbActiveModal) {}

  ngOnInit() {
    this.modifiedReview = JSON.parse(JSON.stringify(this.review));
  }

  isShowReview(review: ProfileReviewData): review is ProfileShowReviewData {
    return review.type === ReviewType.Show;
  }

  isEpisodeReview(review: ProfileReviewData): review is ProfileEpisodeReviewData {
    return review.type === ReviewType.Episode;
  }

  changesMade() {
    return JSON.stringify(this.modifiedReview) !== JSON.stringify(this.review);
  }

  changesSaved() {
    this.activeModal.close(this.modifiedReview);
  }
}
