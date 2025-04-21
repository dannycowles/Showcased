import {Component, OnInit} from '@angular/core';
import {ProfileService} from '../../../services/profile.service';
import {WatchlistData} from '../../../data/lists/watchlist-data';

@Component({
  selector: 'app-profile-watchlist-page',
  templateUrl: './profile-watchlist-page.component.html',
  styleUrl: './profile-watchlist-page.component.css',
  standalone: false
})
export class ProfileWatchlistPageComponent implements OnInit {
  watchlistEntries: WatchlistData[];

  constructor(private profileService: ProfileService) { };

  async ngOnInit() {

    // Retrieve all watchlist entries for profile
    try {
      this.watchlistEntries = await this.profileService.getFullWatchlist();
    } catch(error) {
      console.error(error);
    }
  }

  async removeShowFromWatchlist(removeId: number) {
    try {
      await this.profileService.removeShowFromWatchlist(removeId);

      // Remove the show from the entries shown to the user
      this.watchlistEntries = this.watchlistEntries.filter(show => show.showId != removeId);
    } catch(error) {
      console.error(error);
    }
  }

  async moveShowToWatchingList(moveId: number) {
    try {
      await this.profileService.moveShowToWatchingList(moveId);

      // Remove the show from the entries shown to the user
      this.watchlistEntries = this.watchlistEntries.filter(show => show.showId != moveId);
    } catch(error) {
      console.error(error);
    }
  }

}
