import {Component} from '@angular/core';
import {UserSearchData} from '../../../data/user-search-data';
import {UserService} from '../../../services/user.service';
import {UtilsService} from '../../../services/utils.service';
import {FormsModule} from '@angular/forms';
import {NgOptimizedImage} from '@angular/common';
import {RouterLink} from '@angular/router';

@Component({
  selector: 'app-user-search-page',
  templateUrl: './user-search-page.component.html',
  styleUrl: './user-search-page.component.css',
  imports: [FormsModule, NgOptimizedImage, RouterLink],
  standalone: true,
})
export class UserSearchPageComponent {
  searchString: string = '';
  searchResults: UserSearchData[] | null = null;
  debouncedSearchUsers: () => void;
  isLoading: boolean = false;
  hasSearched: boolean = false;

  constructor(
    private userService: UserService,
    public utilsService: UtilsService,
  ) {
    this.debouncedSearchUsers = this.utilsService.debounce(() =>
      this.searchUsers(),
    );
  }

  async searchUsers() {
    if (this.searchString.trim().length > 0) {
      this.isLoading = true;
      this.hasSearched = true;

      try {
        this.searchResults = await this.userService.searchUsers(
          this.searchString,
        );
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
}
