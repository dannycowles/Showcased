import {Component, OnInit} from '@angular/core';
import {ProfileService} from '../../../services/profile.service';
import {ActivatedRoute, Router, RouterLink} from '@angular/router';
import {SingleCollectionData} from '../../../data/single-collection-data';
import {FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators} from '@angular/forms';
import {UpdateCollectionDetails} from '../../../data/dto/update-collection-details';
import {NgClass, NgOptimizedImage} from '@angular/common';
import {ConfirmationService} from '../../../services/confirmation.service';

@Component({
  selector: 'app-profile-edit-collection-page',
  imports: [FormsModule, ReactiveFormsModule, RouterLink, NgClass, NgOptimizedImage],
  templateUrl: './profile-edit-collection-details-page.component.html',
  styleUrl: './profile-edit-collection-details-page.component.css',
  standalone: true,
})
export class ProfileEditCollectionDetailsPageComponent implements OnInit {
  readonly collectionId: number;
  collectionDetails: SingleCollectionData;

  readonly maxNameLength = 100;
  readonly minNameLength = 5;

  readonly maxDescriptionLength = 250;

  editMessage: string = '';
  editSuccess: boolean;
  selectedVisibility: string = "Public - Anyone can view";

  editCollectionForm = new FormGroup({
    collectionName: new FormControl('', [
      Validators.required,
      Validators.minLength(this.minNameLength),
      Validators.maxLength(this.maxNameLength),
    ]),
    description: new FormControl('', [
      Validators.maxLength(this.maxDescriptionLength),
    ]),
    isPrivate: new FormControl(false),
    isRanked: new FormControl(false),
  });
  originalCollectionDetails: UpdateCollectionDetails;

  constructor(private profileService: ProfileService,
              private route: ActivatedRoute,
              private router: Router,
              private confirmationService: ConfirmationService) {
    this.collectionId = this.route.snapshot.params['id'];
  }

  async ngOnInit() {
    try {
      this.collectionDetails = await this.profileService.getCollectionDetails(this.collectionId);

      this.editCollectionForm.setValue({
        collectionName: this.collectionDetails.name,
        description: this.collectionDetails.description,
        isPrivate: this.collectionDetails.isPrivate,
        isRanked: this.collectionDetails.isRanked,
      });

      // Store original collection details to use to determine if changes need to be saved
      this.originalCollectionDetails = { ...this.editCollectionForm.value };
    } catch (error) {
      console.error(error);
    }
  }

  isSaveDisabled(): boolean {
    return JSON.stringify(this.editCollectionForm.value) === JSON.stringify(this.originalCollectionDetails) || this.editCollectionForm.invalid;
  }

  setVisibility(isPrivate: boolean) {
    this.editCollectionForm.patchValue({isPrivate: isPrivate});
    this.selectedVisibility = isPrivate ? 'Private - Only you can view' : 'Public - Anyone can view';
  }

  async saveEdits() {
    try {
      const currentValues: UpdateCollectionDetails = this.editCollectionForm.value;
      const updatedData: Partial<UpdateCollectionDetails> = {};

      // Only add fields that were actually modified
      for (const key in currentValues) {
        const typedKey = key as keyof UpdateCollectionDetails;

        if (currentValues[typedKey] !== this.originalCollectionDetails[typedKey]) {
          (updatedData as any)[typedKey] = currentValues[typedKey];
        }
      }

      const response = await this.profileService.updateCollection(this.collectionId, updatedData);
      if (response.ok) {
        this.editMessage = 'Changes saved!';
        this.editSuccess = true;
        this.originalCollectionDetails = { ...this.editCollectionForm.value };
      } else {
        this.editMessage = 'You already have a collection with this name.';
        this.editSuccess = false;
      }
    } catch (error) {
      console.error();
    } finally {
      setTimeout(() => (this.editMessage = ''), 5000);
    }
  }

  async deleteCollection() {
    const confirmation = await this.confirmationService.confirmDeleteCollection(this.collectionDetails.name);
    if (confirmation) {
      try {
        const response = await this.profileService.deleteCollection(this.collectionId);

        if (response.ok) {
          this.router.navigate(['/profile/collections']);
        }
      } catch (error) {
        console.error(error);
      }
    }
  }
}
