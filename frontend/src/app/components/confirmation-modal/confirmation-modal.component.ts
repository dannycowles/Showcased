import {Component, Input} from '@angular/core';
import {NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-confirmation-modal',
  imports: [],
  templateUrl: './confirmation-modal.component.html',
  styleUrl: './confirmation-modal.component.css',
  standalone: true
})
export class ConfirmationModalComponent {
  @Input({required: true}) itemName: string;

  constructor(public activeModal: NgbActiveModal) {}
}
