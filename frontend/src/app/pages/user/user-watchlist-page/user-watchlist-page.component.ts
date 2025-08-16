import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {UserService} from '../../../services/user.service';
import {ShowListData} from '../../../data/lists/show-list-data';
import {Title} from '@angular/platform-browser';

@Component({
  selector: 'app-user-watchlist-page',
  templateUrl: './user-watchlist-page.component.html',
  styleUrl: './user-watchlist-page.component.css',
  standalone: false
})
export class UserWatchlistPageComponent implements OnInit {
  readonly username: string;
  watchlistEntries: ShowListData[];

  constructor(private route: ActivatedRoute,
              private userService: UserService,
              private title: Title) {
    this.username = this.route.snapshot.params['username'];
    this.title.setTitle(`${this.username}'s Watchlist | Showcased`);
  };

  async ngOnInit() {
    // Retrieve full watchlist for user
    try {
      this.watchlistEntries = await this.userService.getFullWatchlist(this.username);
    } catch(error) {
      console.error(error);
    }
  };
}
