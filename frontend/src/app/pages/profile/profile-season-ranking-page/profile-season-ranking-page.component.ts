import {Component, OnInit} from '@angular/core';
import {ProfileService} from '../../../services/profile.service';

@Component({
  selector: 'app-profile-season-ranking-page',
  templateUrl: './profile-season-ranking-page.component.html',
  styleUrl: './profile-season-ranking-page.component.css',
  standalone: false
})
export class ProfileSeasonRankingPageComponent implements OnInit{

  constructor(private profileService: ProfileService) { };

  ngOnInit() {};

}
