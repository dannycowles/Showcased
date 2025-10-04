import {Component, Input, OnInit} from '@angular/core';
import {ProfileService} from '../../services/profile.service';
import {UtilsService} from '../../services/utils.service';
import {UserSearchData} from '../../data/user-search-data';
import {NgOptimizedImage} from '@angular/common';
import {RouterLink} from '@angular/router';
import {FormsModule} from '@angular/forms';
import {UserService} from '../../services/user.service';
import {PageData} from '../../data/page-data';
import {InfiniteScrollDirective} from 'ngx-infinite-scroll';

@Component({
  selector: 'app-followers-following',
  imports: [NgOptimizedImage, RouterLink, FormsModule, InfiniteScrollDirective],
  templateUrl: './followers-following.component.html',
  styleUrl: './followers-following.component.css',
  standalone: true,
})
export class FollowersFollowingComponent implements OnInit {
  @Input({ required: true }) listType: 'followers' | 'following';
  @Input() editable: boolean = false; // true for profile page, false for user page
  @Input() username: string;

  searchString: string = '';
  debouncedSearch: () => void;
  searchResults: PageData<UserSearchData>;

  readonly followHandlers = {
    getFollowers: (page: number) => {
      if (this.editable) {
        return this.profileService.getFollowersList(this.searchString, page);
      } else {
        return this.userService.getFollowersList(this.username, this.searchString, page);
      }
    },
    getFollowing: (page: number) => {
      if (this.editable) {
        return this.profileService.getFollowingList(this.searchString, page);
      } else {
        return this.userService.getFollowingList(this.username, this.searchString, page);
      }
    }
  }

  constructor(
    private profileService: ProfileService,
    private userService: UserService,
    private utils: UtilsService,
  ) {}

  ngOnInit() {
    this.debouncedSearch = this.utils.debounce(() => {
      this.search();
    });
    this.search();
  }

  async search() {
    try {
      if (this.listType === "followers") {
        this.searchResults = await this.followHandlers.getFollowers(1);
      } else {
        this.searchResults = await this.followHandlers.getFollowing(1);
      }
    } catch (error) {
      console.error(error);
    }
  }

  async loadMoreUsers() {
    console.log(this.searchResults.page.number);
    // If all users have been loaded, return
    if (this.searchResults.page.number + 1 >= this.searchResults.page.totalPages) {
      return;
    }

    try {
      let result;
      if (this.listType === "followers") {
        result = await this.followHandlers.getFollowers(this.searchResults.page.number + 2);
      } else {
        result = await this.followHandlers.getFollowing(this.searchResults.page.number + 2);
      }
      this.searchResults.content.push(...result.content);
      this.searchResults.page = result.page;
    } catch (error) {
      console.error(error);
    }
  }

  async follow(userId: number) {
    try {
      const response = await this.userService.followUser(userId);
      if (response.ok) {
        const updateUser = this.searchResults.content.find(
          (user) => user.id === userId,
        );
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
        const updateUser = this.searchResults.content.find(
          (user) => user.id === userId,
        );
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
        this.searchResults.content = this.searchResults.content.filter(
          (user) => user.id != userId,
        );
      }
    } catch (error) {
      console.error(error);
    }
  }
}
