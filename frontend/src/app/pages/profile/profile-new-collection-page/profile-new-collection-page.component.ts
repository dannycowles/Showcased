import {Component} from '@angular/core';
import {ProfileService} from '../../../services/profile.service';
import {FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators} from '@angular/forms';
import {Router, RouterLink} from '@angular/router';
import {CreateCollectionDto} from '../../../data/dto/create-collection-dto';
import {CollectionData} from '../../../data/collection-data';
import {NgOptimizedImage} from '@angular/common';

@Component({
  selector: 'app-profile-new-collection-page',
  imports: [ReactiveFormsModule, FormsModule, NgOptimizedImage, RouterLink],
  templateUrl: './profile-new-collection-page.component.html',
  styleUrl: './profile-new-collection-page.component.css',
  standalone: true,
})
export class ProfileNewCollectionPageComponent {
  readonly maxNameLength = 100;
  readonly minNameLength = 5;

  readonly maxDescriptionLength = 250;

  errorMessage: string = "";
  selectedVisibility: string = "Public - Anyone can view";

  newCollectionForm = new FormGroup({
    collectionName: new FormControl('', [
      Validators.required,
      Validators.minLength(this.minNameLength),
      Validators.maxLength(this.maxNameLength),
    ]),
    description: new FormControl('', [
      Validators.maxLength(this.maxDescriptionLength),
    ]),
    privateCollection: new FormControl(false),
    ranked: new FormControl(false)
  });

  constructor(private profileService: ProfileService,
              private router: Router) {};

  async createCollection() {
    try {
      const data: CreateCollectionDto = this.newCollectionForm.value as CreateCollectionDto;
      const response = await this.profileService.createCollection(data);

      if (response.ok) {
        const newCollection: CollectionData = await response.json();
        this.router.navigate(['/profile/collections', newCollection.id]);
        return;
      } else {
        this.errorMessage = "You already have a collection with this name";
      }
    } catch (error) {
      console.error(error);
    } finally {
      setTimeout(() => this.errorMessage = "", 5000);
    }
  }

  setVisibility(isPrivate: boolean) {
    this.newCollectionForm.patchValue({privateCollection: isPrivate});
    this.selectedVisibility = isPrivate ? 'Private - Only you can view' : 'Public - Anyone can view';
  }

  cancelCreateCollection() {
    this.router.navigate(['/profile/collections']);
  }
}
