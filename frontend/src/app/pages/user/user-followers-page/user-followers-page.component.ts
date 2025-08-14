import {Component} from '@angular/core';
import {ActivatedRoute} from '@angular/router';

@Component({
  selector: 'app-user-followers-page',
  templateUrl: './user-followers-page.component.html',
  styleUrl: './user-followers-page.component.css',
  standalone: false
})
export class UserFollowersPageComponent {
  readonly username: string;

  constructor(private route: ActivatedRoute) {
    this.username = this.route.snapshot.params['username'];
  };
}
