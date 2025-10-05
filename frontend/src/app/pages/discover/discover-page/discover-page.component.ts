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
    try {
      const [trending, topRated, genres] = await Promise.all([
        this.showService.fetchTrendingShows(),
        this.showService.fetchTopRatedShows(),
        this.showService.fetchShowGenres()
      ]);

      this.trendingShows = trending;
      this.topRatedShows = topRated;
      this.showGenres = genres;
    } catch (error) {
      console.error(error);
    }
  }
}
