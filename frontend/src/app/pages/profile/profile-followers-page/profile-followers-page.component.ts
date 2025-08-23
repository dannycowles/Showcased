import {Component} from '@angular/core';
import {FollowersFollowingComponent} from '../../../components/followers-following/followers-following.component';
import {RouterLink} from '@angular/router';

@Component({
  selector: 'app-profile-followers-page',
  templateUrl: './profile-followers-page.component.html',
  styleUrl: './profile-followers-page.component.css',
  imports: [FollowersFollowingComponent, RouterLink],
  standalone: true,
})
export class ProfileFollowersPageComponent {}
