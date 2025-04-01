import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {ShowService} from '../../services/show.service';
import {EpisodeData} from '../../data/episode-data';
import {UtilsService} from '../../services/utils.service';
import {ProfileService} from '../../services/profile.service';
import {ToastDisplayService} from '../../services/toast.service';

@Component({
  selector: 'app-episode-page',
  templateUrl: './episode-page.component.html',
  styleUrl: './episode-page.component.css',
  standalone: false
})
export class EpisodePageComponent implements OnInit {
  showId: number;
  seasonNumber: number;
  episodeNumber: number;
  episode: EpisodeData;

  constructor(private route: ActivatedRoute,
              private showService: ShowService,
              private profileService: ProfileService,
              private toastService: ToastDisplayService,
              public utilsService: UtilsService) { }

  async ngOnInit() {
    this.showId = this.route.snapshot.params['id'];
    this.seasonNumber = this.route.snapshot.params['seasonNumber'];
    this.episodeNumber = this.route.snapshot.params['episodeNumber'];

    // Fetch episode details from the backend
    try {
      this.episode = await this.showService.fetchEpisodeDetails(this.showId, this.seasonNumber, this.episodeNumber);
    } catch (error) {
      console.error(error);
    }
  }

  returnToSeasonDetails() {
    window.location.href = `/show/${this.showId}/season/${this.seasonNumber}`;
  }

  // Add the episode to the user's ranking list
  async addToRankingList() {
    try {
      let data = {
        "showId": this.showId,
        "episodeTitle": this.episode.name,
        "season": this.seasonNumber,
        "episode": this.episodeNumber,
        "posterPath": this.episode.stillPath
      };
      let response = await this.profileService.addEpisodeToRankingList(data);

      if (response.status === 201) {
        this.toastService.addToEpisodeRankingToast(this.episode.name);
      }
    } catch (error) {
    console.error(error);}
  }
}
