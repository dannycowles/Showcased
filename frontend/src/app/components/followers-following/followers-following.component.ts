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
  @Input() editable: boolean = true; // true for profile page, false for user page
  @Input() userId: number;

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
      let action;
      if (this.editable) {
        action = (this.listType === "followers")
          ? () => this.profileService.getFollowersList(this.searchString)
          : () => this.profileService.getFollowingList(this.searchString);
      } else {
        action = (this.listType === "followers")
          ? () => this.userService.getFollowersList(this.userId)
          : () => this.userService.getFollowingList(this.userId);
      }

      this.searchResults = await action();
    } catch (error) {
      console.error(error);
    }
  }

  async follow(userId: number) {
    try {
      const response = await this.userService.followUser(userId);
      if (response.ok) {
        const updateUser = this.searchResults.find(user => user.id === userId);
        updateUser.isFollowing = true;
      }
    } catch (error) {
      console.error(error);
    }
  }

  async unfollow(userId: number) {
    try {
      const response = await this.userService.unfollowUser(userId);
      if (response.ok) {
        const updateUser = this.searchResults.find(user => user.id === userId);
        updateUser.isFollowing = false;
      }
    } catch (error) {
      console.error(error);
    }
  }

  async remove(userId: number) {
    try {
      const response = await this.profileService.removeFollower(userId);
      if (response.ok) {
        this.searchResults = this.searchResults.filter(user => user.id != userId);
      }
    } catch(error) {
      console.error(error);
    }
  }
}
