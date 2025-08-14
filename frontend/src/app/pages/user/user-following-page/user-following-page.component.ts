import {Component} from '@angular/core';
import {ActivatedRoute} from '@angular/router';

@Component({
  selector: 'app-user-following-page',
  templateUrl: './user-following-page.component.html',
  styleUrl: './user-following-page.component.css',
  standalone: false
})
export class UserFollowingPageComponent {
  readonly username: string;

  constructor(private route: ActivatedRoute) {
    this.username = this.route.snapshot.params['username'];
  };
}
