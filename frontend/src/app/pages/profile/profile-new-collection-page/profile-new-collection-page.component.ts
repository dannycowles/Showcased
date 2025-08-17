import { Component } from '@angular/core';
import {ProfileService} from '../../../services/profile.service';
import {FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators} from '@angular/forms';

@Component({
  selector: 'app-profile-new-collection-page',
  imports: [ReactiveFormsModule, FormsModule],
  templateUrl: './profile-new-collection-page.component.html',
  styleUrl: './profile-new-collection-page.component.css',
  standalone: true,
})
export class ProfileNewCollectionPageComponent {
  readonly maxNameLength = 100;
  readonly minNameLength = 5;

  readonly maxDescriptionLength = 250;

  newCollectionForm = new FormGroup({
    name: new FormControl('', [
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

  constructor(private profileService: ProfileService) {}

}
