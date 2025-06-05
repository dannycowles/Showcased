import {Component, OnInit} from '@angular/core';
import {ProfileService} from '../../../services/profile.service';
import {SeasonRankingData} from '../../../data/lists/season-ranking-data';

@Component({
  selector: 'app-profile-season-ranking-page',
  templateUrl: './profile-season-ranking-page.component.html',
  styleUrl: './profile-season-ranking-page.component.css',
  standalone: false
})
export class ProfileSeasonRankingPageComponent implements OnInit{
  rankingEntries: SeasonRankingData[];

  constructor(private profileService: ProfileService) { };

  async ngOnInit() {
    try {
      this.rankingEntries = await this.profileService.getSeasonRankingList();
    } catch (error) {
      console.error(error);
    }
  };

  async removeSeasonFromRankingList(removeId: number) {
    try {
      await this.profileService.removeSeasonFromRankingList(removeId);

      // Remove the season from entries shown to user
      this.rankingEntries = this.rankingEntries.filter(season => season.id != removeId);
    } catch(error) {
      console.error(error);
    }
  }

  async updateSeasonRankingList() {
    try {
      let updates = this.rankingEntries.map(season => ({
        "id": season.id,
        "rankNum": season.rankNum
      }));
      await this.profileService.updateSeasonRankingList(updates);
    } catch(error) {
      console.error(error);
    }
  }

}
