import {Component, OnInit} from '@angular/core';
import {ProfileService} from '../../../services/profile.service';
import {UserSearchData} from '../../../data/user-search-data';

@Component({
  selector: 'app-profile-following-page',
  templateUrl: './profile-following-page.component.html',
  styleUrl: './profile-following-page.component.css',
  standalone: false
})
export class ProfileFollowingPageComponent implements OnInit {
  followingEntries: UserSearchData[]

  constructor(private profileService: ProfileService) {};

  async ngOnInit() {
    try {
      this.followingEntries = await this.profileService.getFollowingList();
    } catch(error) {
      console.error(error);
    }
  };

}
