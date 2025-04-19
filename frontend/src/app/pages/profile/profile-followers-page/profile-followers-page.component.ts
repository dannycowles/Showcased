import {Component, OnInit} from '@angular/core';
import {UserSearchData} from '../../../data/user-search-data';
import {ProfileService} from '../../../services/profile.service';
import {UserService} from '../../../services/user.service';

@Component({
  selector: 'app-profile-followers-page',
  templateUrl: './profile-followers-page.component.html',
  styleUrl: './profile-followers-page.component.css',
  standalone: false
})
export class ProfileFollowersPageComponent implements OnInit {
  followersEntries: UserSearchData[];

  constructor(private profileService: ProfileService,
              private userService: UserService) {};

  async ngOnInit() {
    try {
      this.followersEntries = await this.profileService.getFollowersList();
    } catch(error) {
      console.error(error);
    }
  };

  async removeFollower(followerId: number) {
    try {
      await this.userService.removeFollower(followerId);
      this.followersEntries = this.followersEntries.filter(user => user.id != followerId);
    } catch(error) {
      console.error(error);
    }
  }

}
