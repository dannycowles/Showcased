import {Component, Input, OnInit} from '@angular/core';
import {NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';
import {FormsModule} from '@angular/forms';
import {ProfileReviewData, ReviewData} from '../../data/types';
import {ReviewPageType, ReviewType} from '../../data/enums';
import {ProfileEpisodeReviewData, ProfileShowReviewData} from '../../data/profile-reviews-data';
import {EpisodeReviewData, ShowReviewData} from '../../data/reviews-data';

@Component({
  selector: 'app-edit-review-modal',
  imports: [FormsModule],
  templateUrl: './edit-review-modal.component.html',
  styleUrl: './edit-review-modal.component.css',
  standalone: true,
})
export class EditReviewModalComponent implements OnInit {
  @Input({ required: true }) review: ProfileReviewData | ReviewData;
  modifiedReview: ProfileReviewData | ReviewData;
  readonly maxCommentaryLength = 5000;

  constructor(public activeModal: NgbActiveModal) {}

  ngOnInit() {
    this.modifiedReview = structuredClone(this.review);
  }

  isShowReview(review: ProfileReviewData | ReviewData): review is ProfileShowReviewData | ShowReviewData {
    return review.type === ReviewType.Show || review.type === ReviewPageType.ShowPage;
  }

  isEpisodeReview(review: ProfileReviewData | ReviewData): review is ProfileEpisodeReviewData | EpisodeReviewData {
    return review.type === ReviewType.Episode || review.type === ReviewPageType.EpisodePage;
  }

  changesMade() {
    return JSON.stringify(this.modifiedReview) !== JSON.stringify(this.review);
  }

  changesSaved() {
    this.activeModal.close(this.modifiedReview);
  }
}
