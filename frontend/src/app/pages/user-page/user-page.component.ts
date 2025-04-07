import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {ProfileData} from '../../data/profile-data';
import {UserService} from '../../services/user.service';
import {UtilsService} from '../../services/utils.service';

@Component({
  selector: 'app-user-page',
  templateUrl: './user-page.component.html',
  styleUrl: './user-page.component.css',
  standalone: false
})
export class UserPageComponent implements OnInit {
  userId: number;
  userDetails: ProfileData;
  numTop: number = 10; // Holds the number of entries shown per list on showcase page

  constructor(private route: ActivatedRoute,
              private userService: UserService,
              public utilsService: UtilsService) {}

  async ngOnInit() {
    this.userId = this.route.snapshot.params['id'];

    // Retrieve user details from the backend
    try {
      this.userDetails = await this.userService.getUserDetails(this.userId);
    } catch(error) {
      console.error(error);
    }
  }

  viewFullWatchlist() {
    window.location.href += '/watchlist';
  }

  viewFullWatchingList() {
    window.location.href += '/watching';
  }
}
