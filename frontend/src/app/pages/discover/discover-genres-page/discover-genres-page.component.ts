import {Component, OnInit} from '@angular/core';
import {ShowService} from '../../../services/show.service';
import {ActivatedRoute, Router} from '@angular/router';
import {ResultPageData} from '../../../data/show/result-page-data';

@Component({
  selector: 'app-discover-genres-page',
  templateUrl: './discover-genres-page.component.html',
  styleUrl: './discover-genres-page.component.css',
  standalone: false
})
export class DiscoverGenresPageComponent implements OnInit {
  resultData: ResultPageData;
  genre: number;
  page: number;

  constructor(private showService: ShowService,
              private route: ActivatedRoute,
              private router: Router) {
  this.genre = +this.route.snapshot.queryParams['genre'];
  };

  ngOnInit() {
    this.route.queryParams.subscribe(async params => {
      this.page = +params['page'] || 1;
      try {
        this.resultData = await this.showService.searchByGenre(this.genre, this.page);
      } catch(error) {
        this.router.navigate(['/not-found']);
      }
    });
  };

  navigateToPage(page: number) {
    this.page = page;
    this.router.navigate([], {
      queryParams: {
        genre: this.genre,
        page: this.page}
    });
  }
}
