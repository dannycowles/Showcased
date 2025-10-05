import {Component, OnInit} from '@angular/core';
import {ProfileService} from '../../../services/profile.service';
import {SeasonRankingData} from '../../../data/lists/season-ranking-data';
import {SearchResultData} from '../../../data/search-result-data';
import {RouterLink} from '@angular/router';
import {NgOptimizedImage} from '@angular/common';
import {SeasonListFullComponent} from '../../../components/season-list-full/season-list-full.component';

@Component({
  selector: 'app-profile-season-ranking-page',
  templateUrl: './profile-season-ranking-page.component.html',
  styleUrl: './profile-season-ranking-page.component.css',
  imports: [
    RouterLink,
    NgOptimizedImage,
    SeasonListFullComponent,
  ],
  standalone: true,
})
export class ProfileSeasonRankingPageComponent implements OnInit {
  rankingEntries: SeasonRankingData[] = [];
  loadingData: boolean = true;
  selectedSeason: number = 1;
  selectedShow: SearchResultData | null = null;

  constructor(private profileService: ProfileService) {}

  async ngOnInit() {
    try {
      this.rankingEntries = await this.profileService.getSeasonRankingList();
    } catch (error) {
      console.error(error);
    } finally {
      this.loadingData = false;
    }
  }
}
