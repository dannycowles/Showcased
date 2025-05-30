import {Component} from '@angular/core';
import {ActivatedRoute} from '@angular/router';

@Component({
  selector: 'app-user-followers-page',
  templateUrl: './user-followers-page.component.html',
  styleUrl: './user-followers-page.component.css',
  standalone: false
})
export class UserFollowersPageComponent {
  readonly userId: number;

  constructor(private route: ActivatedRoute) {
    this.userId = this.route.snapshot.params['id'];
  };
}
