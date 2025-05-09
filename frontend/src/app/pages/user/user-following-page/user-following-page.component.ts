import {Component, OnInit} from '@angular/core';
import {UserService} from '../../../services/user.service';
import {ActivatedRoute} from '@angular/router';
import {UserSearchData} from '../../../data/user-search-data';

@Component({
  selector: 'app-user-following-page',
  templateUrl: './user-following-page.component.html',
  styleUrl: './user-following-page.component.css',
  standalone: false
})
export class UserFollowingPageComponent implements OnInit {
  followingEntries: UserSearchData[];
  readonly userId: number;

  constructor(private userService: UserService,
              private route: ActivatedRoute) {
    this.userId = this.route.snapshot.params['id'];
  };

  async ngOnInit() {
    try {
      this.followingEntries = await this.userService.getFollowingList(this.userId);
    } catch(error) {
      console.error(error);
    }
  };

  async followUser(user: UserSearchData) {
    try {
      const response: Response = await this.userService.followUser(user.id);

      if (response.ok) {
        user.isFollowing = true;
      }
    } catch(error) {
      console.error(error);
    }
  }

  async unfollowUser(user: UserSearchData) {
    try {
      const response: Response = await this.userService.unfollowUser(user.id);

      if (response.ok) {
        user.isFollowing = false;
      }
    } catch(error) {
      console.error(error);
    }
  }
}
