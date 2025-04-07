import {Component, OnInit} from '@angular/core';
import {UserService} from '../../services/user.service';
import {ActivatedRoute} from '@angular/router';
import {WatchingData} from '../../data/watching-data';

@Component({
  selector: 'app-user-watching-page',
  templateUrl: './user-watching-page.component.html',
  styleUrl: './user-watching-page.component.css',
  standalone: false
})
export class UserWatchingPageComponent implements OnInit {
  userId: number;
  watchingEntries: WatchingData[];

  constructor(private route: ActivatedRoute,
              private userService: UserService) { };

  async ngOnInit() {
    this.userId = this.route.snapshot.params['id'];

    // Retrieve full watching list for the user
    try {
      this.watchingEntries = await this.userService.getFullWatchingList(this.userId);
    } catch(error) {
      console.error(error);
    }
  };

  returnToUserProfile() {
    window.location.href = `/user/${this.userId}`;
  }

}
