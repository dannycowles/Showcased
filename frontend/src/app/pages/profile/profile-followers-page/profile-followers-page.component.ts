import {Component} from '@angular/core';
import {FollowersFollowingComponent} from '../../../components/followers-following/followers-following.component';
import {RouterLink} from '@angular/router';
import {NgOptimizedImage} from '@angular/common';

@Component({
  selector: 'app-profile-followers-page',
  templateUrl: './profile-followers-page.component.html',
  styleUrl: './profile-followers-page.component.css',
  imports: [FollowersFollowingComponent, RouterLink, NgOptimizedImage],
  standalone: true,
})
export class ProfileFollowersPageComponent {}
