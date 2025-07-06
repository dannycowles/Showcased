import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {ShowService} from '../../../services/show.service';
import {EpisodeData} from '../../../data/show/episode-data';
import {ProfileService} from '../../../services/profile.service';
import {ToastDisplayService} from '../../../services/toast.service';
import {UtilsService} from '../../../services/utils.service';


@Component({
  selector: 'app-episode-page',
  templateUrl: './episode-page.component.html',
  styleUrl: './episode-page.component.css',
  standalone: false
})
export class EpisodePageComponent implements OnInit {
  readonly showId: number;
  readonly seasonNumber: number;
  readonly episodeNumber: number;
  episode: EpisodeData;

  constructor(private route: ActivatedRoute,
              private showService: ShowService,
              private profileService: ProfileService,
              private toastService: ToastDisplayService,
              public utilsService: UtilsService) {
    this.showId = this.route.snapshot.params['id'];
    this.seasonNumber = this.route.snapshot.params['seasonNumber'];
    this.episodeNumber = this.route.snapshot.params['episodeNumber'];
  }

  async ngOnInit() {
    // Fetch episode details from the backend
    try {
      this.episode = await this.showService.fetchEpisodeDetails(this.showId, this.seasonNumber, this.episodeNumber);
    } catch (error) {
      console.error(error);
    }
  }

  // Add the episode to the user's ranking list
  async addToRankingList() {
    try {
      const data = {
        "id": this.episode.id,
        "showId": this.showId,
        "showTitle": this.episode.showTitle,
        "episodeTitle": this.episode.episodeTitle,
        "season": this.seasonNumber,
        "episode": this.episodeNumber,
        "posterPath": this.episode.stillPath
      };

      const response = await this.profileService.addEpisodeToRankingList(data);
      if (response.ok) {
        this.toastService.addToEpisodeRankingToast(this.episode.episodeTitle);
      }
    } catch (error) {
    console.error(error);}
  }
}
