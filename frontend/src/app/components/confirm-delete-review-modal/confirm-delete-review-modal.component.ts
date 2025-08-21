import {Component, Input} from '@angular/core';
import {NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-confirm-delete-review-modal',
  imports: [],
  templateUrl: './confirm-delete-review-modal.component.html',
  styleUrl: './confirm-delete-review-modal.component.css',
  standalone: true
})
export class ConfirmDeleteReviewModalComponent {
  @Input({required: true}) itemName: string;

  constructor(public activeModal: NgbActiveModal) {}

}
