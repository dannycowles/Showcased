import {Component} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Title} from '@angular/platform-browser';

@Component({
  selector: 'app-user-followers-page',
  templateUrl: './user-followers-page.component.html',
  styleUrl: './user-followers-page.component.css',
  standalone: false
})
export class UserFollowersPageComponent {
  readonly username: string;

  constructor(private route: ActivatedRoute,
              private title: Title) {
    this.username = this.route.snapshot.params['username'];
    this.title.setTitle(`${this.username}'s Followers | Showcased`);
  };
}
