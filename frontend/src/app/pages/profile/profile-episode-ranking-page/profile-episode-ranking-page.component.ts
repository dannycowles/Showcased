import {Component, OnInit} from '@angular/core';
import {ProfileService} from '../../../services/profile.service';
import {EpisodeRankingData} from '../../../data/lists/episode-ranking-data';

@Component({
  selector: 'app-profile-episode-ranking-page',
  templateUrl: './profile-episode-ranking-page.component.html',
  styleUrl: './profile-episode-ranking-page.component.css',
  standalone: false
})
export class ProfileEpisodeRankingPageComponent implements OnInit {
  rankingEntries: EpisodeRankingData[];

  constructor(private profileService: ProfileService) { }

  async ngOnInit() {
    // Retrieve episode ranking entries for the profile
    try {
      this.rankingEntries = await this.profileService.getFullEpisodeRankingList();
    } catch(error) {
      console.error(error);
    }
  }

  async removeEpisodeFromRankingList(removeId: number) {
    try {
      await this.profileService.removeEpisodeFromRankingList(removeId);

      // Remove the episode from entries shown to the user
      this.rankingEntries = this.rankingEntries.filter(show => show.id !== removeId);
    } catch(error) {
      console.error(error);
    }
  }

  async updateEpisodeRankingList() {
    try {
      const updates = this.rankingEntries.map(episode => ({
        "episodeId": episode.id,
        "rankNum": episode.rankNum
      }));
      await this.profileService.updateEpisodeRankingList(updates);
    } catch(error) {
      console.error(error);
    }
  }
}
