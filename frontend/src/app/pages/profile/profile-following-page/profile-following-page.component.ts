import {Component, OnInit} from '@angular/core';
import {ProfileService} from '../../../services/profile.service';
import {UserSearchData} from '../../../data/user-search-data';
import {UserService} from '../../../services/user.service';

@Component({
  selector: 'app-profile-following-page',
  templateUrl: './profile-following-page.component.html',
  styleUrl: './profile-following-page.component.css',
  standalone: false
})
export class ProfileFollowingPageComponent implements OnInit {
  followingEntries: UserSearchData[]

  constructor(private profileService: ProfileService,
              private userService: UserService) {};

  async ngOnInit() {
    try {
      this.followingEntries = await this.profileService.getFollowingList();
    } catch(error) {
      console.error(error);
    }
  };

  async unfollowUser(unfollowId: number) {
    try {
      await this.userService.unfollowUser(unfollowId);
      this.followingEntries = this.followingEntries.filter(user => user.id != unfollowId);
    } catch(error) {
      console.error(error);
    }
  }

}
