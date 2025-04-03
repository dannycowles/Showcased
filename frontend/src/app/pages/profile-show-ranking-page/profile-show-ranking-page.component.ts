import {Component, OnInit} from '@angular/core';
import {ProfileService} from '../../services/profile.service';

@Component({
  selector: 'app-profile-show-ranking-page',
  templateUrl: './profile-show-ranking-page.component.html',
  styleUrl: './profile-show-ranking-page.component.css',
  standalone: false
})
export class ProfileShowRankingPageComponent implements OnInit {

  constructor(private profileService: ProfileService) { }

  ngOnInit() {}

}
