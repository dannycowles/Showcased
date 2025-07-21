import {Component, OnInit} from '@angular/core';
import {ActivityData} from '../../../data/activity-data';
import {ProfileService} from '../../../services/profile.service';

@Component({
  selector: 'app-profile-activity-page',
  templateUrl: './profile-activity-page.component.html',
  styleUrl: './profile-activity-page.component.css',
  standalone: false
})
export class ProfileActivityPageComponent implements OnInit {
  activityData: ActivityData[];

  constructor(private profileService: ProfileService) {};

  async ngOnInit() {
    try {
      this.activityData = await this.profileService.getProfileActivity();
    } catch (error) {
      console.error(error);
    }
  }
}
