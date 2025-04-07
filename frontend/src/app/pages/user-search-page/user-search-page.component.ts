import {Component, OnInit} from '@angular/core';
import {UserSearchData} from '../../data/user-search-data';
import {UserService} from '../../services/user.service';

@Component({
  selector: 'app-user-search-page',
  templateUrl: './user-search-page.component.html',
  styleUrl: './user-search-page.component.css',
  standalone: false
})
export class UserSearchPageComponent implements OnInit {
  searchString: string;
  searchResults: UserSearchData[];

  constructor(private userService: UserService) {};

  ngOnInit() {};

  async search() {
    // As long as the search string is not empty we will call the backend
    if (this.searchString) {
      try {
        this.searchResults = await this.userService.searchUsers(this.searchString);
        console.log('searchResults', this.searchResults);
      } catch(error) {
        console.error(error);
      }
    }
  }

}
