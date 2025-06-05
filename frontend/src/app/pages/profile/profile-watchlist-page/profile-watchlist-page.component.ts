import {Component, OnInit} from '@angular/core';
import {ProfileService} from '../../../services/profile.service';
import {SearchResultData} from '../../../data/search-result-data';
import {ShowListData} from '../../../data/lists/show-list-data';

@Component({
  selector: 'app-profile-watchlist-page',
  templateUrl: './profile-watchlist-page.component.html',
  styleUrl: './profile-watchlist-page.component.css',
  standalone: false
})
export class ProfileWatchlistPageComponent implements OnInit {
  watchlistEntries: ShowListData[];
  modalMessage: string;
  modalColor: string;

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

  async handleAddShow(show: SearchResultData) {
    try {
      const data = {
        showId: show.id,
        title: show.name,
        posterPath: show.posterPath
      };
      const response = await this.profileService.addShowToWatchlist(data);

      if (response.ok) {
        this.modalMessage = "Successfully added!";
        this.modalColor = "green";
        this.watchlistEntries.push(new ShowListData(data));
      } else if (response.status === 409) {
        this.modalMessage = "You already have this show on your watchlist.";
        this.modalColor = "red";
      }
    } catch (error) {
      console.error(error);
    } finally {
      setTimeout(() => {
        this.modalMessage = "";
      }, 3000);
    }
  }
}
