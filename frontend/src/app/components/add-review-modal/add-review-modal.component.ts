import {Component, Input} from '@angular/core';
import {NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';
import {UtilsService} from '../../services/utils.service';
import {FormsModule} from '@angular/forms';

@Component({
  selector: 'app-add-review-modal',
  imports: [FormsModule],
  templateUrl: './add-review-modal.component.html',
  styleUrl: './add-review-modal.component.css',
  standalone: true,
})
export class AddReviewModalComponent {
  @Input({required: true}) modalTitle: string;
  rating: number = 0;
  commentary: string = "";
  containsSpoilers: boolean = false;
  readonly maxCommentaryLength = 5000;

  constructor(public activeModal: NgbActiveModal,
              public utilsService: UtilsService) {};

  submitReview() {
    this.activeModal.close({
      rating: this.rating,
      commentary: this.commentary,
      containsSpoilers: this.containsSpoilers
    });
  }
}
