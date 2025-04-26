import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {SeasonData} from '../../../data/show/season-data';
import {ShowService} from '../../../services/show.service';
import {UtilsService} from '../../../services/utils.service';
import {ProfileService} from '../../../services/profile.service';


@Component({
  selector: 'app-season-page',
  templateUrl: './season-page.component.html',
  styleUrl: './season-page.component.css',
  standalone: false
})
export class SeasonPageComponent implements OnInit {
  readonly showId: number;
  seasonNumber: number;
  season: SeasonData
  numSeasons: number;

  constructor(private route: ActivatedRoute,
              private showService: ShowService,
              public utilsService: UtilsService,
              private profileService: ProfileService) {
    this.showId = this.route.snapshot.params['id'];
    this.route.params.subscribe(params => {
      this.seasonNumber = params['seasonNumber'];
      this.retrieveSeasonInfo();
    });
  }

  async ngOnInit() {
    await this.retrieveSeasonInfo();

    // Retrieve number of seasons from backend
    try {
      this.numSeasons = await this.showService.fetchNumberOfSeasons(this.showId);
    } catch(error) {
      console.error(error);
    }
  }

  async retrieveSeasonInfo() {
    // Retrieve season details from backend
    try {
      this.season = await this.showService.fetchSeasonDetails(this.showId, this.seasonNumber);
    } catch(error) {
      console.error(error);
    }
  }

  async addSeasonToRankingList() {
    try {
      const data = {
        id: this.season.id,
        showId: this.showId,
        season: this.seasonNumber,
        posterPath: this.season.posterPath,
        showTitle: this.season.showTitle
      };

      const response = await this.profileService.addSeasonToRankingList(data);
      if (response.ok) {
        this.season.onRankingList = true;
      }
    } catch(error) {
      console.error(error);
    }
  }

  async removeSeasonRankingList() {
    try {
      const response = await this.profileService.removeSeasonFromRankingList(this.season.id);
      if (response.ok) {
        this.season.onRankingList = false;
      }
    } catch(error) {
      console.error(error);
    }
  }

  seasonSelected(seasonNumber: number) {
    // Check if the user tries to select the same season they are already on
    if (seasonNumber != this.seasonNumber) {
      window.location.href = `show/${this.showId}/season/${seasonNumber}`;
    }
  }
}
