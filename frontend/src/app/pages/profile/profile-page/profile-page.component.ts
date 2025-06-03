import {Component, OnInit} from '@angular/core';
import {ProfileData} from '../../../data/profile-data';
import {ProfileService} from '../../../services/profile.service';
import {UtilsService} from '../../../services/utils.service';
import {UserSocialData} from '../../../data/user-social-data';

@Component({
  selector: 'app-profile-page',
  templateUrl: './profile-page.component.html',
  styleUrl: './profile-page.component.css',
  standalone: false
})
export class ProfilePageComponent implements OnInit {
  profileData: ProfileData;
  objectUrl: string | null = null;
  newBio: string;
  readonly bioMaxLength: number = 100;
  bioMessage: string | null = null;
  debouncedAddSocialAccount: (social: UserSocialData) => void;

  constructor(private profileService: ProfileService,
              public utilsService: UtilsService) { }

  async ngOnInit() {
    // Retrieve profile data from backend
    try {
      this.profileData = await this.profileService.getProfileDetails();
      this.newBio = this.profileData.bio;
    } catch (error) {
      console.error(error);
    }

    this.debouncedAddSocialAccount = this.utilsService.debounce((social: UserSocialData) => {
      this.addSocialAccount(social);
    });
  }

  async onFileSelected(event: any) {
    let file: File = event.target.files[0];

    // Immediately show the preview image without waiting for S3
    this.objectUrl = URL.createObjectURL(file);
    this.profileData.profilePicture = this.objectUrl;

    if (file) {
      let formData: FormData = new FormData();
      formData.append('file', file);

      try {
        this.profileData.profilePicture = await this.profileService.uploadProfilePicture(formData) + "?t=" + new Date().getTime();

        // Clean up the object URL
        URL.revokeObjectURL(this.objectUrl);
      } catch(error) {
        console.error(error);
      }
    }
  }

  async updateBio() {
    // Prevent sending unchanged bio
    if (this.newBio === this.profileData.bio) {
      return;
    }

    try {
      const data = {
        bio: this.newBio
      };

      const response = await this.profileService.updateProfileDetails(data);
      if (response.ok) {
        this.profileData.bio = this.newBio;
        this.bioMessage = "Bio updated successfully.";
      }
    } catch (error) {
      console.error(error);
    } finally {
      setTimeout(() => {
        this.bioMessage = "";
      }, 3000);
    }
  }

  async removeSocialAccount(social: UserSocialData) {
    try {
      const response = await this.profileService.removeSocialAccount(social.socialId);

      if (response.ok) {
        social.handle = null;
      }
    } catch (error) {
      console.error(error);
    }
  }

  addSocialAccount(social: UserSocialData) {
    try {
      const data = {
        socialId: social.socialId,
        handle: social.handle
      };
      this.profileService.addSocialAccount(data);
    } catch (error) {
      console.error(error);
    }
  }
}
