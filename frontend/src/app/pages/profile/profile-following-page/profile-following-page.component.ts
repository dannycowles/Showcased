import {Component} from '@angular/core';
import {FollowersFollowingComponent} from '../../../components/followers-following/followers-following.component';
import {RouterLink} from '@angular/router';

@Component({
  selector: 'app-profile-following-page',
  templateUrl: './profile-following-page.component.html',
  styleUrl: './profile-following-page.component.css',
  imports: [FollowersFollowingComponent, RouterLink],
  standalone: true,
})
export class ProfileFollowingPageComponent {}
