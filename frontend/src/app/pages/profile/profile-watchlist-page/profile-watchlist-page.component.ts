import {Component, OnInit} from '@angular/core';
import {ProfileService} from '../../../services/profile.service';
import {ShowListData} from '../../../data/lists/show-list-data';
import {ShowListFullComponent} from '../../../components/show-list-full/show-list-full.component';
import {RouterLink} from '@angular/router';
import {NgOptimizedImage} from '@angular/common';

@Component({
  selector: 'app-profile-watchlist-page',
  templateUrl: './profile-watchlist-page.component.html',
  styleUrl: './profile-watchlist-page.component.css',
  imports: [ShowListFullComponent, RouterLink, NgOptimizedImage],
  standalone: true,
})
export class ProfileWatchlistPageComponent implements OnInit {
  watchlistEntries: ShowListData[] = [];
  loadingData: boolean = true;

  constructor(private profileService: ProfileService) {}

  async ngOnInit() {
    try {
      this.watchlistEntries = await this.profileService.getFullWatchlist();
    } catch (error) {
      console.error(error);
    } finally {
      this.loadingData = false;
    }
  }
}
