import {Component, OnInit} from '@angular/core';
import {UserData} from '../../../data/user-data';
import {ProfileService} from '../../../services/profile.service';
import {UtilsService} from '../../../services/utils.service';
import {ReviewType} from '../../../data/enums';

@Component({
  selector: 'app-profile-page',
  templateUrl: './profile-page.component.html',
  styleUrl: './profile-page.component.css',
  standalone: false
})
export class ProfilePageComponent implements OnInit {
  profileData: UserData;

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

  protected readonly ReviewType = ReviewType;
}
