import {Component, OnInit} from '@angular/core';
import {ProfileData} from '../../data/profile-data';
import {ProfileService} from '../../services/profile.service';
import {UtilsService} from '../../services/utils.service';

@Component({
  selector: 'app-profile-page',
  templateUrl: './profile-page.component.html',
  styleUrl: './profile-page.component.css',
  standalone: false
})
export class ProfilePageComponent implements OnInit {
  profileData: ProfileData;

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

  manageWatchlistPressed() {
    window.location.href = 'profile/watchlist';
  }

  manageWatchingPressed() {
    window.location.href = 'profile/watching';
  }

  manageShowRankingPressed() {
    window.location.href = 'profile/show-ranking';
  }

  manageEpisodeRankingPressed() {
    window.location.href = 'profile/episode-ranking';
  }

}
