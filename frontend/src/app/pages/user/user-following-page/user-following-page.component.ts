import {Component} from '@angular/core';
import {ActivatedRoute, RouterLink} from '@angular/router';
import {Title} from '@angular/platform-browser';
import {FollowersFollowingComponent} from '../../../components/followers-following/followers-following.component';
import {NgOptimizedImage} from '@angular/common';

@Component({
  selector: 'app-user-following-page',
  templateUrl: './user-following-page.component.html',
  styleUrl: './user-following-page.component.css',
  imports: [FollowersFollowingComponent, RouterLink, NgOptimizedImage],
  standalone: true,
})
export class UserFollowingPageComponent {
  readonly username: string;

  constructor(
    private route: ActivatedRoute,
    private title: Title,
  ) {
    this.username = this.route.snapshot.params['username'];
    this.title.setTitle(`${this.username}'s Following List | Showcased`);
  }
}
