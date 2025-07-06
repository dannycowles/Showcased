import {Component} from '@angular/core';
import {ShowService} from '../../../services/show.service';
import {ResultPageData} from '../../../data/show/result-page-data';
import {UtilsService} from '../../../services/utils.service';
import {SearchResultData} from '../../../data/search-result-data';

@Component({
  selector: 'app-search-page',
  templateUrl: './search-page.component.html',
  styleUrl: './search-page.component.css',
  standalone: false
})
export class SearchPageComponent {
  searchString: string = "";
  searchResults: ResultPageData;
  debouncedSearchShows: () => void;
  isLoading: boolean = false;
  hasSearched: boolean = false;

  constructor(private showService: ShowService,
              public utilsService: UtilsService) {
    this.debouncedSearchShows = this.utilsService.debounce(() => this.searchShows());
  }

  async searchShows() {
    if (this.searchString.trim().length > 0) {
      try {
        this.isLoading = true;
        this.hasSearched = true;
        this.searchResults = await this.showService.searchForShows(this.searchString);
      } catch (error) {
        console.error(error);
      } finally {
        this.isLoading = false;
      }
    } else {
      this.hasSearched = false;
      this.searchResults = null;
    }
  }

  showYears(show: SearchResultData): string {
    if (show.startYear === show.endYear) {
      return `${show.startYear}`;
    } else if (show.endYear == null) {
      return `${show.startYear} - `;
    } else {
      return `${show.startYear} - ${show.endYear}`
    }
  }
}
