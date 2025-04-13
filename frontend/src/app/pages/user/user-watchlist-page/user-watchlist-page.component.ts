import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {WatchlistData} from '../../../data/lists/watchlist-data';
import {UserService} from '../../../services/user.service';

@Component({
  selector: 'app-user-watchlist-page',
  templateUrl: './user-watchlist-page.component.html',
  styleUrl: './user-watchlist-page.component.css',
  standalone: false
})
export class UserWatchlistPageComponent implements OnInit {
  readonly userId: number;
  watchlistEntries: WatchlistData[];

  constructor(private route: ActivatedRoute,
              private userService: UserService) {
    this.userId = this.route.snapshot.params['id'];
  };

  async ngOnInit() {
    // Retrieve full watchlist for user
    try {
      this.watchlistEntries = await this.userService.getFullWatchlist(this.userId);
    } catch(error) {
      console.error(error);
    }
  };

  returnToUserProfile() {
    window.location.href = `/user/${this.userId}`;
  }

}
