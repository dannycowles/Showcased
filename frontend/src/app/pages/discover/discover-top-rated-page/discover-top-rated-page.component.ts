import {Component, OnInit} from '@angular/core';
import {ResultPageData} from '../../../data/show/result-page-data';
import {ShowService} from '../../../services/show.service';
import {ActivatedRoute, Router} from '@angular/router';

@Component({
  selector: 'app-discover-top-rated-page',
  templateUrl: './discover-top-rated-page.component.html',
  styleUrl: './discover-top-rated-page.component.css',
  standalone: false
})
export class DiscoverTopRatedPageComponent implements OnInit {
  resultData: ResultPageData;
  page: number = 1;
  isLoading: boolean = false;

  constructor(private showService: ShowService,
              private router: Router,
              private route: ActivatedRoute) {};

  ngOnInit() {
    this.route.queryParams.subscribe(async params => {
      this.page = +params["page"];

      try {
        this.isLoading = true;
        this.resultData = await this.showService.fetchTopRatedShows(this.page);
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
        page: this.page
      }
    });
  }
}
