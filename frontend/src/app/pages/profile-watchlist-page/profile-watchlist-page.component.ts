import {Component, OnInit} from '@angular/core';
import {ProfileService} from '../../services/profile.service';

@Component({
  selector: 'app-profile-watchlist-page',
  templateUrl: './profile-watchlist-page.component.html',
  styleUrl: './profile-watchlist-page.component.css',
  standalone: false
})
export class ProfileWatchlistPageComponent implements OnInit {

  constructor(private profileService: ProfileService) { };

  ngOnInit() {

  }

}
