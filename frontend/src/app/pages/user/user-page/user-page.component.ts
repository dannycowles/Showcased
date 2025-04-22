import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {ProfileData} from '../../../data/profile-data';
import {UserService} from '../../../services/user.service';
import {UtilsService} from '../../../services/utils.service';

@Component({
  selector: 'app-user-page',
  templateUrl: './user-page.component.html',
  styleUrl: './user-page.component.css',
  standalone: false
})
export class UserPageComponent implements OnInit {
  readonly userId: number;
  userDetails: ProfileData;

  constructor(private route: ActivatedRoute,
              private userService: UserService,
              public utilsService: UtilsService) {
    this.userId = this.route.snapshot.params['id'];
  }

  async ngOnInit() {
    // Retrieve user details from the backend
    try {
      this.userDetails = await this.userService.getUserDetails(this.userId);
    } catch(error) {
      console.error(error);
    }
  }

  async followUser() {
    try {
      const response = await this.userService.followUser(this.userId);

      if (response.ok) {
        this.userDetails.isFollowing = true;
        this.userDetails.numFollowers += 1;
      }
    } catch(error) {
      console.error(error);
    }
  }

  async unfollowUser() {
    try {
      const response = await this.userService.unfollowUser(this.userId);

      if (response.ok) {
        this.userDetails.isFollowing = false;
        this.userDetails.numFollowers -= 1;
      }
    } catch(error) {
      console.error(error);
    }
  }
}
