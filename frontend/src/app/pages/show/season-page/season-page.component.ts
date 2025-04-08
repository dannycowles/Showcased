import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {SeasonData} from '../../../data/show/season-data';
import {ShowService} from '../../../services/show.service';
import {UtilsService} from '../../../services/utils.service';


@Component({
  selector: 'app-season-page',
  templateUrl: './season-page.component.html',
  styleUrl: './season-page.component.css',
  standalone: false
})
export class SeasonPageComponent implements OnInit {
  showId: number;
  seasonNumber: number;
  season: SeasonData
  numSeasons: number;

  constructor(private route: ActivatedRoute,
              private showService: ShowService,
              public utilsService: UtilsService) {}

  async ngOnInit() {
    this.showId = this.route.snapshot.params['id'];
    this.seasonNumber = this.route.snapshot.params['seasonNumber'];

    // Retrieve season details from backend
    try {
      this.season = await this.showService.fetchSeasonDetails(this.showId, this.seasonNumber);
    } catch(error) {
      console.error(error);
    }

    // Retrieve number of seasons from backend
    try {
      this.numSeasons = await this.showService.fetchNumberOfSeasons(this.showId);
    } catch(error) {
      console.error(error);
    }
  }

  episodeSelected(episodeNumber: number) {
    window.location.href = `${window.location.pathname}/episode/${episodeNumber}`;
  }

  seasonSelected(seasonNumber: number) {
    // Check if the user tries to select the same season they are already on
    if (seasonNumber != this.seasonNumber) {
      window.location.href = `show/${this.showId}/season/${seasonNumber}`;
    }
  }

  returnToShowDetails() {
    window.location.href = `show/${this.showId}`;
  }
}
