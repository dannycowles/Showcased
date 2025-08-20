import {Component, CUSTOM_ELEMENTS_SCHEMA, OnInit} from '@angular/core';
import {ProfileService} from '../../../services/profile.service';
import {RouterLink} from '@angular/router';
import {ProfileSettingsData} from '../../../data/profile-settings-data';
import {FormControl, FormsModule, ReactiveFormsModule} from '@angular/forms';
import {UpdateProfileDetailsDto} from '../../../data/dto/update-profile-details-dto';
import {NgClass, NgOptimizedImage} from '@angular/common';
import {UserSocialData} from '../../../data/user-social-data';
import {AddSocialDto} from '../../../data/dto/add-social-dto';

@Component({
  selector: 'app-profile-settings-page',
  imports: [
    RouterLink,
    ReactiveFormsModule,
    NgOptimizedImage,
    NgClass,
    FormsModule,
  ],
  templateUrl: './profile-settings-page.component.html',
  styleUrl: './profile-settings-page.component.css',
  standalone: true,
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class ProfileSettingsPageComponent implements OnInit {
  profileSettings: ProfileSettingsData;

  readonly maxBioLength: number = 250;

  bio = new FormControl('');
  saveMessage: string = '';

  objectUrl: string | null = null;
  avatarMessage: string = '';
  avatarUploadSuccess: boolean;

  originalSocialAccounts: UserSocialData[];
  socialMessage: string = '';

  constructor(private profileService: ProfileService) {}

  async ngOnInit() {
    try {
      this.profileSettings = await this.profileService.getProfileSettings();
      this.bio.setValue(this.profileSettings.bio);
      this.originalSocialAccounts = this.profileSettings.socialAccounts.map(social => ({ ...social }));
    } catch (error) {
      console.error(error);
    }
  }

  isProfileSaveDisabled() {
    return this.bio.value === this.profileSettings.bio;
  }

  isSocialSaveDisabled() {
    return JSON.stringify(this.originalSocialAccounts) === JSON.stringify(this.profileSettings.socialAccounts);
  }

  async saveProfileChanges() {
    try {
      const data: UpdateProfileDetailsDto = {
        bio: this.bio.value,
      };

      const response = await this.profileService.updateProfileDetails(data);
      if (response.ok) {
        this.saveMessage = 'Saved changes!';
        this.profileSettings.bio = this.bio.value;
      }
    } catch (error) {
      console.error(error);
    } finally {
      setTimeout(() => (this.saveMessage = ''), 5000);
    }
  }

  async onFileSelected(event: Event) {
    const input = event.target as HTMLInputElement;
    const file: File = input.files[0];

    // Immediately show the preview image without waiting for S3
    this.objectUrl = URL.createObjectURL(file);
    this.profileSettings.profilePicture = this.objectUrl;

    if (file) {
      const formData: FormData = new FormData();
      formData.append('file', file);

      try {
        const response =
          await this.profileService.uploadProfilePicture(formData);

        if (response.ok) {
          this.profileSettings.profilePicture =
            (await response.text()) + '?t=' + new Date().getTime();
          this.avatarMessage = 'Successfully updated your avatar!';
          this.avatarUploadSuccess = true;
        } else if (response.status === 413) {
          this.avatarMessage =
            'Uploaded file is too large. Maximum size is 5MB.';
          this.avatarUploadSuccess = false;
        } else if (response.status === 415) {
          this.avatarMessage =
            'Invalid file type. The supported types are JPEG & PNG.';
          this.avatarUploadSuccess = false;
        } else {
          this.avatarMessage =
            'There was an error updating your avatar. Please try again.';
          this.avatarUploadSuccess = false;
        }
      } catch (error) {
        console.error(error);
      } finally {
        // Clean up the object URL
        URL.revokeObjectURL(this.objectUrl);
        setTimeout(() => (this.avatarMessage = ''), 5000);
      }
    }
  }

  async removeSocialAccount(social: UserSocialData) {
    if (this.originalSocialAccounts.find(s => s.socialId === social.socialId).handle === null) {
      social.handle = null;
      return;
    }

    try {
      const response = await this.profileService.removeSocialAccount(social.socialId);
      if (response.ok) {
        social.handle = null;
        this.originalSocialAccounts = this.profileSettings.socialAccounts.map(social => ({ ...social }));
      }
    } catch (error) {
      console.error(error);
    }
  }

  async addSocialAccounts() {
    const data: AddSocialDto[] = this.profileSettings.socialAccounts
      .filter(social => social.handle !== null)
      .map(social => ({
        socialId: social.socialId,
        handle: social.handle
      }));

    try {
      const response = await this.profileService.addSocialAccounts(data);

      if (response.ok) {
        // Update original social accounts to match the current state
        this.originalSocialAccounts = this.profileSettings.socialAccounts.map(social => ({...social}));
        this.socialMessage = "Successfully updated socials!"
      }
    } catch (error) {
      console.error(error);
    } finally {
      setTimeout(() => this.socialMessage = '', 5000);
    }
  }
}
