import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {ShowService} from '../../services/show.service';
import {EpisodeData} from '../../data/episode-data';
import {UtilsService} from '../../services/utils.service';

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
}
