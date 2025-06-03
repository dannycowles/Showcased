import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {UserService} from '../../../services/user.service';
import {ShowListData} from '../../../data/lists/show-list-data';

@Component({
  selector: 'app-user-watching-page',
  templateUrl: './user-watching-page.component.html',
  styleUrl: './user-watching-page.component.css',
  standalone: false
})
export class UserWatchingPageComponent implements OnInit {
  readonly userId: number;
  watchingEntries: ShowListData[];

  constructor(private route: ActivatedRoute,
              private userService: UserService) {
    this.userId = this.route.snapshot.params['id'];
  };

  async ngOnInit() {
    // Retrieve full watching list for the user
    try {
      this.watchingEntries = await this.userService.getFullWatchingList(this.userId);
    } catch(error) {
      console.error(error);
    }
  };
}
