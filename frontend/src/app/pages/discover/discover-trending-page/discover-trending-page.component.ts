import {Component, OnInit} from '@angular/core';
import {ShowService} from '../../../services/show.service';
import {ResultPageData} from '../../../data/show/result-page-data';
import {ActivatedRoute, Router, RouterLink} from '@angular/router';
import {NgOptimizedImage} from '@angular/common';

@Component({
  selector: 'app-discover-trending-page',
  templateUrl: './discover-trending-page.component.html',
  styleUrl: './discover-trending-page.component.css',
  imports: [RouterLink, NgOptimizedImage],
  standalone: true,
})
export class DiscoverTrendingPageComponent implements OnInit {
  resultData: ResultPageData;
  page: number = 1;
  isLoading: boolean = false;

  constructor(
    private showService: ShowService,
    private router: Router,
    private route: ActivatedRoute,
  ) {}

  ngOnInit() {
    this.route.queryParams.subscribe(async (params) => {
      this.page = +params['page'];

      try {
        this.isLoading = true;
        this.resultData = await this.showService.fetchTrendingShows(this.page);
      } catch (error) {
        console.error(error);
      } finally {
        this.isLoading = false;
      }
    });
  }

  navigateToPage(newPage: number) {
    this.page = newPage;
    this.router.navigate([], {
      queryParams: {
        page: this.page,
      },
    });
  }
}
