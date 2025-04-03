import {Component, OnInit} from '@angular/core';
import {ProfileService} from '../../services/profile.service';
import {WatchlistData} from '../../data/watchlist-data';

@Component({
  selector: 'app-profile-watchlist-page',
  templateUrl: './profile-watchlist-page.component.html',
  styleUrl: './profile-watchlist-page.component.css',
  standalone: false
})
export class ProfileWatchlistPageComponent implements OnInit {
  watchlistData: WatchlistData;

  constructor(private profileService: ProfileService) { };

  async ngOnInit() {

    // Retrieve watchlist details for profile
    try {
      this.watchlistData = await this.profileService.getFullWatchlist();
    } catch(error) {
      console.error(error)
    }
  }

}
