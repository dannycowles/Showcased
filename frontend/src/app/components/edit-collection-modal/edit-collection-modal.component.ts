import {Component, Input, OnInit} from '@angular/core';
import {UtilsService} from '../../services/utils.service';
import {ProfileService} from '../../services/profile.service';
import {NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';
import {FormsModule} from '@angular/forms';
import {UpdateCollectionDetails} from '../../data/dto/update-collection-details';

@Component({
  selector: 'app-edit-collection-modal',
  imports: [FormsModule],
  templateUrl: './edit-collection-modal.component.html',
  styleUrl: './edit-collection-modal.component.css',
  standalone: true,
})
export class EditCollectionModalComponent implements OnInit {
  @Input({ required: true }) collectionId: number;
  @Input({ required: true }) collectionName: string;
  @Input({ required: true }) collectionDescription: string;
  @Input({ required: true }) onUpdate: (updates: {}) => void;
  message: string = "";
  messageColor: string = "";

  // Used to store the names before edits are made, in order to disable "Save Changes" button as needed
  originalName: string;
  originalDescription: string;

  readonly maxNameLength = 100;
  readonly maxDescriptionLength = 250;

  constructor(public utilsService: UtilsService,
              private profileService: ProfileService,
              public activeModal: NgbActiveModal) {};

  ngOnInit() {
    this.originalName = this.collectionName;
    this.originalDescription = this.collectionDescription;
  }

  get hasChanges(): boolean {
    return this.originalName !== this.collectionName || this.originalDescription !== this.collectionDescription;
  }

  async updateCollectionDetails() {
    try {
      // Prevent user from entering blank collection name
      if (this.collectionName.trim().length === 0) {
        this.message = "Collection name cannot be empty";
        this.messageColor = "red";
        return;
      }

      const updates: UpdateCollectionDetails = {};
      if (this.originalName !== this.collectionName) updates.collectionName = this.collectionName;
      if (this.originalDescription !== this.collectionDescription) updates.description = this.collectionDescription;

      const response = await this.profileService.updateCollection(this.collectionId, updates);
      if (response.ok) {
        this.message = "Changes saved!";
        this.messageColor = "green";

        // Update "original" values after successful update
        if (updates.collectionName) this.originalName = updates.collectionName;
        if (updates.description) this.originalDescription = updates.description;

        // Send back data to parent (calling component)
        this.onUpdate(updates);
      } else if (response.status == 409) {
        this.message = "You already have a collection with that name";
        this.messageColor = "red";
      }
    } catch (error) {
      console.error(error);
    } finally {
      setTimeout(() => (this.message = ""), 3000);
    }
  }
}
