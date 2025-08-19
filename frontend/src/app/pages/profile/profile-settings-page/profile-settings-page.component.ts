import {Component, OnInit} from '@angular/core';
import {ProfileService} from '../../../services/profile.service';
import {RouterLink} from '@angular/router';
import {ProfileSettingsData} from '../../../data/profile-settings-data';
import {FormControl, ReactiveFormsModule} from '@angular/forms';
import {UpdateProfileDetailsDto} from '../../../data/dto/update-profile-details-dto';

@Component({
  selector: 'app-profile-settings-page',
  imports: [RouterLink, ReactiveFormsModule],
  templateUrl: './profile-settings-page.component.html',
  styleUrl: './profile-settings-page.component.css',
  standalone: true,
})
export class ProfileSettingsPageComponent implements OnInit {
  profileSettings: ProfileSettingsData;

  readonly maxBioLength: number = 250;

  bio = new FormControl('');
  saveMessage: string = "";

  constructor(private profileService: ProfileService) {}

  async ngOnInit() {
    try {
      this.profileSettings = await this.profileService.getProfileSettings();
      this.bio.setValue(this.profileSettings.bio);
    } catch (error) {
      console.error(error);
    }
  }

  isProfileSaveDisabled() {
    return this.bio.value === this.profileSettings.bio;
  }

  async saveProfileChanges() {
    try {
      const data: UpdateProfileDetailsDto = {
        bio: this.bio.value
      }

      const response = await this.profileService.updateProfileDetails(data);
      if (response.ok) {
        this.saveMessage = "Saved changes!";
        this.profileSettings.bio = this.bio.value;
      }
    } catch (error) {
      console.error(error);
    } finally {
      setTimeout(() => this.saveMessage = "", 5000);
    }
  }
}
