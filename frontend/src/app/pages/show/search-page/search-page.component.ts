import {Component, OnInit} from '@angular/core';
import {ShowService} from '../../../services/show.service';
import {ResultPageData} from '../../../data/show/result-page-data';


@Component({
  selector: 'app-search-page',
  templateUrl: './search-page.component.html',
  styleUrl: './search-page.component.css',
  standalone: false
})
export class SearchPageComponent implements OnInit {
  searchString: string;
  searchResults: ResultPageData;

  constructor(private showService: ShowService) {}

  ngOnInit() {
  }

  async search() {

    // As long as the search is not blank, send a request to the backend to get results
    if (this.searchString) {
      try {
        this.searchResults = await this.showService.searchForShows(this.searchString);
      } catch (error) {
        console.error(error);
      }
    }
  }
}
