import {Component, OnInit} from '@angular/core';
import {UserSearchData} from '../../../data/user-search-data';
import {UserService} from '../../../services/user.service';
import {ActivatedRoute} from '@angular/router';

@Component({
  selector: 'app-user-followers-page',
  templateUrl: './user-followers-page.component.html',
  styleUrl: './user-followers-page.component.css',
  standalone: false
})
export class UserFollowersPageComponent implements OnInit {
  followersEntries: UserSearchData[];
  readonly userId: number;

  constructor(private userService: UserService,
              private route: ActivatedRoute) {
    this.userId = this.route.snapshot.params['id'];
  };

  async ngOnInit() {
    try {
      this.followersEntries = await this.userService.getFollowersList(this.userId);
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
