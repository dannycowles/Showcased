import {Component, Input} from '@angular/core';
import {NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-confirm-delete-collection-modal',
  imports: [],
  templateUrl: './confirm-delete-collection-modal.component.html',
  styleUrl: './confirm-delete-collection-modal.component.css',
  standalone: true
})
export class ConfirmDeleteCollectionModalComponent {
  @Input({required: true}) itemName: string;

  constructor(public activeModal: NgbActiveModal) {};
}
