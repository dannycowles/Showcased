import {Injectable} from '@angular/core';
import {ConfirmationModalComponent} from '../components/confirmation-modal/confirmation-modal.component';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {ConfirmDeleteReviewModalComponent} from '../components/confirm-delete-review-modal/confirm-delete-review-modal.component';

@Injectable({
  providedIn: 'root'
})
export class ConfirmationService {

  constructor(private modalService: NgbModal) {}

  async confirmRemove(itemName: string): Promise<boolean> {
    const modalRef = this.modalService.open(ConfirmationModalComponent, {
      centered: true,
      ariaLabelledBy: "confirmRemoveModal"
    });
    modalRef.componentInstance.itemName = itemName;

    // true indicates the user selected remove, whereas false indicates the user selected cancel/esc/closed the modal
    try {
      await modalRef.result;
      return true;
    } catch {
      return false;
    }
  }

  async confirmDeleteReview(itemName: string): Promise<boolean> {
    const modalRef = this.modalService.open(ConfirmDeleteReviewModalComponent, {
      centered: true,
      ariaLabelledBy: 'confirmDeleteReviewModal'
    });
    modalRef.componentInstance.itemName = itemName;

    // true indicates the user selected remove, whereas false indicates the user selected cancel/esc/closed the modal
    try {
      await modalRef.result;
      return true;
    } catch {
      return false;
    }
  }
}
