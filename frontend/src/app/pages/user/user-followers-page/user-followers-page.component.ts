import {Component} from '@angular/core';
import {ActivatedRoute, RouterLink} from '@angular/router';
import {Title} from '@angular/platform-browser';
import {FollowersFollowingComponent} from '../../../components/followers-following/followers-following.component';
import {NgOptimizedImage} from '@angular/common';

@Component({
  selector: 'app-user-followers-page',
  templateUrl: './user-followers-page.component.html',
  styleUrl: './user-followers-page.component.css',
  imports: [FollowersFollowingComponent, RouterLink, NgOptimizedImage],
  standalone: true,
})
export class UserFollowersPageComponent {
  readonly username: string;

  constructor(
    private route: ActivatedRoute,
    private title: Title,
  ) {
    this.username = this.route.snapshot.params['username'];
    this.title.setTitle(`${this.username}'s Followers | Showcased`);
  }
}
