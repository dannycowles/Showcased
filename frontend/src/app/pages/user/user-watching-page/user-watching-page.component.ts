import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {UserService} from '../../../services/user.service';
import {ShowListData} from '../../../data/lists/show-list-data';
import {Title} from '@angular/platform-browser';

@Component({
  selector: 'app-user-watching-page',
  templateUrl: './user-watching-page.component.html',
  styleUrl: './user-watching-page.component.css',
  standalone: false
})
export class UserWatchingPageComponent implements OnInit {
  readonly username: string;
  watchingEntries: ShowListData[];

  constructor(private route: ActivatedRoute,
              private userService: UserService,
              private title: Title) {
    this.username = this.route.snapshot.params['username'];
    this.title.setTitle(`${this.username}'s Watching List | Showcased`);
  };

  async ngOnInit() {
    // Retrieve full watching list for the user
    try {
      this.watchingEntries = await this.userService.getFullWatchingList(this.username);
    } catch(error) {
      console.error(error);
    }
  };
}
