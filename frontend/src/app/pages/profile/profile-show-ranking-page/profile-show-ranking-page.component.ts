import { Component, OnInit } from '@angular/core';
import { ProfileService } from '../../../services/profile.service';
import { ShowRankingData } from '../../../data/lists/show-ranking-data';
import {RouterLink} from '@angular/router';
import {NgOptimizedImage} from '@angular/common';
import {ShowListFullComponent} from '../../../components/show-list-full/show-list-full.component';

@Component({
  selector: 'app-profile-show-ranking-page',
  templateUrl: './profile-show-ranking-page.component.html',
  styleUrl: './profile-show-ranking-page.component.css',
  standalone: true,
  imports: [
    RouterLink,
    NgOptimizedImage,
    ShowListFullComponent,
  ],
})
export class ProfileShowRankingPageComponent implements OnInit {
  rankingEntries: ShowRankingData[];

  constructor(private profileService: ProfileService) {};

  async ngOnInit() {
    try {
      this.rankingEntries = await this.profileService.getFullShowRankingList();
    } catch (error) {
      console.error(error);
    }
  }
}
