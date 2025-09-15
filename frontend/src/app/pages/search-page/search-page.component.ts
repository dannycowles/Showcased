import {Component, OnInit} from '@angular/core';
import {ShowService} from '../../services/show.service';
import {ResultPageData} from '../../data/show/result-page-data';
import {UtilsService} from '../../services/utils.service';
import {SearchResultData} from '../../data/search-result-data';
import {ActivatedRoute, RouterLink} from '@angular/router';
import {FormsModule} from '@angular/forms';
import {NgClass, NgOptimizedImage} from '@angular/common';
import {UserService} from '../../services/user.service';
import {UserSearchData} from '../../data/user-search-data';

@Component({
  selector: 'app-search-page',
  templateUrl: './search-page.component.html',
  styleUrl: './search-page.component.css',
  imports: [RouterLink, FormsModule, NgOptimizedImage, NgClass],
  standalone: true,
})
export class SearchPageComponent implements OnInit {
  searchString: string;
  showResults: ResultPageData;
  userResults: UserSearchData[];
  debouncedSearch: () => void;
  isLoading: boolean = false;
  hasSearched: boolean = false;

  showsSelected: boolean = true; // Start with shows selected by default

  constructor(private showService: ShowService,
              public utilsService: UtilsService,
              private userService: UserService,
              private route: ActivatedRoute) {
    this.debouncedSearch = this.utilsService.debounce(() => this.search());
  }

  ngOnInit() {
    this.route.queryParams.subscribe(async (params) => {
      this.searchString = params['query'];
      if (this.searchString) {
        await this.search();
      }
    });
  }

  async selectShows() {
    // Only search if not already selected (aka user on shows search spamming it would not re-search)
    if (!this.showsSelected) {
      this.showsSelected = true;
      await this.search();
    }
  }

  async selectUsers() {
    // Only search if not already selected (aka user on users search spamming it would not re-search)
    if (this.showsSelected) {
      this.showsSelected = false;
      await this.search();
    }
  }

  async search() {
    if (this.searchString.trim().length > 0) {
      try {
        this.isLoading = true;
        this.hasSearched = true;

        if (this.showsSelected) {
          this.showResults = await this.showService.searchForShows(this.searchString);
        } else {
          this.userResults = await this.userService.searchUsers(this.searchString);
        }
      } catch (error) {
        console.error(error);
      } finally {
        this.isLoading = false;
      }
    } else {
      this.hasSearched = false;
      this.showResults = null;
      this.userResults = null;
    }
  }

  showYears(show: SearchResultData): string {
    if (show.startYear === show.endYear) {
      return `${show.startYear}`;
    } else if (show.endYear == null) {
      return `${show.startYear} - `;
    } else {
      return `${show.startYear} - ${show.endYear}`;
    }
  }
}
