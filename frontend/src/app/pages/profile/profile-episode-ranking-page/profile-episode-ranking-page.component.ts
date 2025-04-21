import {Component, OnInit} from '@angular/core';
import {CdkDragDrop, moveItemInArray} from '@angular/cdk/drag-drop';
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

  async removeEpisodeFromRankingList(removeId: number, season: number, episode: number) {
    try {
      await this.profileService.removeEpisodeFromRankingList(removeId, season, episode);

      // Remove the episode from entries shown to the user
      this.rankingEntries = this.rankingEntries.filter(show => !(show.showId == removeId && show.season == season && show.episode == episode));
    } catch(error) {
      console.error(error);
    }
  }

  drop(event: CdkDragDrop<string[]>) {
    moveItemInArray(this.rankingEntries, event.previousIndex, event.currentIndex);

    // Update the rank numbers based on the index within the updated list
    this.rankingEntries.forEach((episode, index) => {
      episode.rankNum = index + 1;
    });
    this.updateEpisodeRankingList();
  }

  async updateEpisodeRankingList() {
    try {
      let updates = this.rankingEntries.map(show => ({
        "showId": show.showId,
        "rankNum": show.rankNum,
        "season": show.season,
        "episode": show.episode
      }));
      await this.profileService.updateEpisodeRankingList(updates);
    } catch(error) {
      console.error(error);
    }
  }

}
