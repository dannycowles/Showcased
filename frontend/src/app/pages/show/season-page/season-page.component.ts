import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, RouterLink} from '@angular/router';
import {SeasonData} from '../../../data/show/season-data';
import {ShowService} from '../../../services/show.service';
import {UtilsService} from '../../../services/utils.service';
import {ProfileService} from '../../../services/profile.service';
import {AddToSeasonRankingList} from '../../../data/dto/add-to-list-dto';
import {Title} from '@angular/platform-browser';
import {NgOptimizedImage} from '@angular/common';
import {NgbDropdown, NgbDropdownItem, NgbDropdownMenu, NgbDropdownToggle} from '@ng-bootstrap/ng-bootstrap';


@Component({
  selector: 'app-season-page',
  templateUrl: './season-page.component.html',
  styleUrl: './season-page.component.css',
  imports: [
    RouterLink,
    NgOptimizedImage,
    NgbDropdown,
    NgbDropdownMenu,
    NgbDropdownItem,
    NgbDropdownToggle,
  ],
  standalone: true,
})
export class SeasonPageComponent implements OnInit {
  readonly showId: number;
  seasonNumber: number;
  season: SeasonData;
  numSeasons: number;

  constructor(
    private route: ActivatedRoute,
    private showService: ShowService,
    public utilsService: UtilsService,
    private profileService: ProfileService,
    private title: Title,
  ) {
    this.showId = this.route.snapshot.params['id'];
    this.route.params.subscribe((params) => {
      this.seasonNumber = params['seasonNumber'];
      this.retrieveSeasonInfo();
    });
  }

  async ngOnInit() {
    // Retrieve number of seasons from backend
    try {
      this.numSeasons = await this.showService.fetchNumberOfSeasons(
        this.showId,
      );
    } catch (error) {
      console.error(error);
    }
  }

  async retrieveSeasonInfo() {
    // Retrieve season details from backend
    try {
      this.season = await this.showService.fetchSeasonDetails(
        this.showId,
        this.seasonNumber,
      );
      this.title.setTitle(
        `${this.season.showTitle} S${this.seasonNumber} | Showcased`,
      );
    } catch (error) {
      console.error(error);
    }
  }

  async addSeasonToRankingList() {
    try {
      const data: AddToSeasonRankingList = {
        showId: this.showId,
        season: this.seasonNumber,
        posterPath: this.season.posterPath,
        showTitle: this.season.showTitle,
      };

      const response = await this.profileService.addSeasonToRankingList(data);
      if (response.ok) {
        this.season.onRankingList = true;
      }
    } catch (error) {
      console.error(error);
    }
  }

  async removeSeasonFromRankingList() {
    try {
      const response = await this.profileService.removeSeasonFromRankingList(
        this.season.id,
      );
      if (response.ok) {
        this.season.onRankingList = false;
      }
    } catch (error) {
      console.error(error);
    }
  }
}
