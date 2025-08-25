import {Component, OnInit} from '@angular/core';
import {ShowService} from '../../../services/show.service';
import {ShowGenresData} from '../../../data/show-genres-data';
import {RouterLink} from '@angular/router';
import {ResultPageData} from '../../../data/show/result-page-data';
import {NgOptimizedImage} from '@angular/common';

@Component({
  selector: 'app-discover-page',
  templateUrl: './discover-page.component.html',
  styleUrl: './discover-page.component.css',
  imports: [RouterLink, NgOptimizedImage],
  standalone: true,
})
export class DiscoverPageComponent implements OnInit {
  showGenres: ShowGenresData;
  trendingShows: ResultPageData;
  topRatedShows: ResultPageData;

  constructor(private showService: ShowService) {}

  async ngOnInit() {
    // Retrieve the show genres from the backend
    try {
      this.trendingShows = await this.showService.fetchTrendingShows();
      this.topRatedShows = await this.showService.fetchTopRatedShows();
      this.showGenres = await this.showService.fetchShowGenres();
    } catch (error) {
      console.error(error);
    }
  }
}
