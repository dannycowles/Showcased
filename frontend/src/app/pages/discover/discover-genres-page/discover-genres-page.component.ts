import {Component, OnInit} from '@angular/core';
import {ShowService} from '../../../services/show.service';
import {ActivatedRoute, Router, RouterLink} from '@angular/router';
import {GenreResultPageData} from '../../../data/show/result-page-data';
import {NgClass, NgOptimizedImage} from '@angular/common';
import {ShowGenresData} from '../../../data/show-genres-data';

@Component({
  selector: 'app-discover-genres-page',
  templateUrl: './discover-genres-page.component.html',
  styleUrl: './discover-genres-page.component.css',
  imports: [RouterLink, NgOptimizedImage, NgClass],
  standalone: true,
})
export class DiscoverGenresPageComponent implements OnInit {
  resultData: GenreResultPageData;
  selectedGenreId: string;
  page: number;
  isLoading: boolean = false;
  genresData: ShowGenresData;

  constructor(private showService: ShowService,
              private route: ActivatedRoute,
              private router: Router) {}

  ngOnInit() {
    this.route.queryParams.subscribe(async (params) => {
      this.page = +params['page'];
      this.selectedGenreId = params['genre'];

      try {
        this.isLoading = true;
        this.resultData = await this.showService.searchByGenre(+this.selectedGenreId, this.page);

        // Only need to fetch the genres if they aren't populated
        if (!this.genresData) {
          this.genresData = await this.showService.fetchShowGenres();
        }
      } catch (error) {
        this.router.navigate(['/not-found']);
      } finally {
        this.isLoading = false;
      }
    });
  }

  navigateToPage(page: number) {
    this.page = page;
    this.router.navigate([], {
      queryParams: {
        genre: this.selectedGenreId,
        page: this.page,
      },
    });
    window.scrollTo({
      top: 0,
      behavior: 'instant',
    });
  }
}
