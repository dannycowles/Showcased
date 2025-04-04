import {Component, OnInit} from '@angular/core';
import {ProfileService} from '../../services/profile.service';
import {WatchingData} from '../../data/watching-data';

@Component({
  selector: 'app-profile-watching-page',
  templateUrl: './profile-watching-page.component.html',
  styleUrl: './profile-watching-page.component.css',
  standalone: false
})
export class ProfileWatchingPageComponent implements OnInit {
  watchingEntries: WatchingData[];

  constructor(private profileService: ProfileService) { }

  async ngOnInit() {

    // Retrieve watching entries for profile
    try {
      this.watchingEntries = await this.profileService.getFullWatchingList();
    } catch(error) {
      console.error(error);
    }
  }

  returnToProfilePressed() {
    window.location.href = "profile";
  }

  async removeShowFromWatchingList(removeId: number) {
    try {
      await this.profileService.removeShowFromWatchingList(removeId);

      // Remove the show from the entries shown to the user
      this.watchingEntries = this.watchingEntries.filter(show => show.showId != removeId);
    } catch(error) {
      console.error(error);
    }
  }

  async moveShowToRankingList(moveId: number) {
    try {
      await this.profileService.moveShowToRankingList(moveId);

      // Remove the show from the entries shown to the user
      this.watchingEntries = this.watchingEntries.filter(show => show.showId != moveId);
    } catch(error) {
      console.error(error);
    }
  }

}
