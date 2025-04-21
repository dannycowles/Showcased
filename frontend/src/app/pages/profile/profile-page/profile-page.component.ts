import {Component, OnInit} from '@angular/core';
import {ProfileData} from '../../../data/profile-data';
import {ProfileService} from '../../../services/profile.service';
import {UtilsService} from '../../../services/utils.service';

@Component({
  selector: 'app-profile-page',
  templateUrl: './profile-page.component.html',
  styleUrl: './profile-page.component.css',
  standalone: false
})
export class ProfilePageComponent implements OnInit {
  profileData: ProfileData;
  objectUrl: string | null = null;

  constructor(private profileService: ProfileService,
              public utilsService: UtilsService) { }

  async ngOnInit() {
    // Retrieve profile data from backend
    try {
      this.profileData = await this.profileService.getProfileDetails();
    } catch (error) {
      console.error(error);
    }
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

}
