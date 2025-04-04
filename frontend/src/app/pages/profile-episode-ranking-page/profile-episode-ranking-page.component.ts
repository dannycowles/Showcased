import {Component, OnInit} from '@angular/core';
import {ProfileService} from '../../services/profile.service';
import {EpisodeRankingData} from '../../data/episode-ranking-data';

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

  returnToProfilePressed() {
    window.location.href = "profile";
  }

  async removeEpisodeFromRankingList(removeId: number, season: number, episode: number) {
    try {
      await this.profileService.removeEpisodeFromRankingList(removeId, season, episode);

      // Remove the episode from entries shown to the user
      this.rankingEntries = this.rankingEntries.filter(show => !(show.showId == removeId && show.season == season && show.episode == episode));
    } catch(error) {
      console.error(error);
    }
  }

}
