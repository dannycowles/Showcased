import {Component, OnInit} from '@angular/core';
import {ProfileData} from '../../../data/profile-data';
import {ProfileService} from '../../../services/profile.service';
import {UtilsService} from '../../../services/utils.service';
import {UserHeaderData} from '../../../data/user-header-data';

@Component({
  selector: 'app-profile-page',
  templateUrl: './profile-page.component.html',
  styleUrl: './profile-page.component.css',
  standalone: false
})
export class ProfilePageComponent implements OnInit {
  profileData: ProfileData;
  headerData: UserHeaderData;

  constructor(private profileService: ProfileService,
              public utilsService: UtilsService) { }

  async ngOnInit() {
    // Retrieve profile data from backend
    try {
      this.profileData = await this.profileService.getProfileDetails();
      this.headerData = this.profileData.userHeaderData;
    } catch (error) {
      console.error(error);
    }
  }
}
