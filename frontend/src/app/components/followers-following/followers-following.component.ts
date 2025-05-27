import {Component, Input, OnInit} from '@angular/core';
import {ProfileService} from '../../services/profile.service';
import {UtilsService} from '../../services/utils.service';
import {UserSearchData} from '../../data/user-search-data';
import {NgOptimizedImage} from '@angular/common';
import {RouterLink} from '@angular/router';
import {FormsModule} from '@angular/forms';
import {UserService} from '../../services/user.service';

@Component({
  selector: 'app-followers-following',
  imports: [
    NgOptimizedImage,
    RouterLink,
    FormsModule
  ],
  templateUrl: './followers-following.component.html',
  styleUrl: './followers-following.component.css',
  standalone: true
})
export class FollowersFollowingComponent implements OnInit {
  @Input({required: true}) listType: "followers" | "following";

  searchString: string = "";
  debouncedSearch: () => void;
  searchResults: UserSearchData[];

  constructor(private profileService: ProfileService,
              private userService: UserService,
              private utils: UtilsService) { };

  ngOnInit() {
    this.debouncedSearch = this.utils.debounce(() => {
      this.search();
    });
    this.search();
  }

  async search() {
    try {
      const action = (this.listType === "followers")
        ? () => this.profileService.getFollowersList(this.searchString)
        : () => this.profileService.getFollowingList(this.searchString);

      this.searchResults = await action();
    } catch (error) {
      console.error(error);
    }
  }

  async remove(userId: number) {
    try {
      const action = (this.listType === "followers")
        ? () => this.profileService.removeFollower(userId)
        : () => this.userService.unfollowUser(userId);

      const response = await action();
      if (response.ok) {
        this.searchResults = this.searchResults.filter(user => user.id != userId);
      }
    } catch(error) {
      console.error(error);
    }
  }
}
