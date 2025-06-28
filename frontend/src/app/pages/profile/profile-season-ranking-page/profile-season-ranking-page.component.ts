import {Component, OnInit} from '@angular/core';
import {ProfileService} from '../../../services/profile.service';
import {SeasonRankingData} from '../../../data/lists/season-ranking-data';
import {UtilsService} from '../../../services/utils.service';
import {ResultPageData} from '../../../data/show/result-page-data';
import {ShowService} from '../../../services/show.service';

@Component({
  selector: 'app-profile-season-ranking-page',
  templateUrl: './profile-season-ranking-page.component.html',
  styleUrl: './profile-season-ranking-page.component.css',
  standalone: false
})
export class ProfileSeasonRankingPageComponent implements OnInit{
  rankingEntries: SeasonRankingData[];
  searchString: string = "";
  showSearchResults: ResultPageData | null = null;
  selectedShowId: number | null = null;
  numSeasons: number | null = null;
  selectedSeason: number = 1;

  hasSearched: boolean = false;
  isLoading: boolean = false;
  message: string = "";
  messageColor: string;

  debouncedSearchSeasons: () => void;
  debouncedAddSeason: () => void;

  constructor(private profileService: ProfileService,
              private utilsService: UtilsService,
              private showService: ShowService) { };

  async ngOnInit() {
    try {
      this.rankingEntries = await this.profileService.getSeasonRankingList();
    } catch (error) {
      console.error(error);
    }

    this.debouncedSearchSeasons = this.utilsService.debounce(() => this.searchShows());
    this.debouncedAddSeason = this.utilsService.debounce(() => this.addSeasonToRanking());
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

  async searchShows() {
    if (this.searchString.trim().length != 0) {
      this.hasSearched = true;
      this.isLoading = true;

      try {
        this.showSearchResults = await this.showService.searchForShows(this.searchString);
      } catch (error) {
        console.error(error);
      } finally {
        this.isLoading = false;
      }
    } else {
      this.hasSearched = false;
      this.showSearchResults = null;
    }
  }

  get selectedShowTitle(): string {
    if (this.selectedShowId == null || this.showSearchResults == null) return "";
    const selectedShow = this.showSearchResults.results.find(show => show.id === this.selectedShowId);
    return selectedShow != null ? selectedShow.title : "";
  }

  async getNumberOfSeasons() {
    try {
      this.numSeasons = await this.showService.fetchNumberOfSeasons(this.selectedShowId);
    } catch (error) {
      console.error(error);
    }
  }

  async addSeasonToRanking() {
    try {
      const data = {
        showId: this.selectedShowId,
        season: this.selectedSeason,
        showTitle: this.selectedShowTitle
      }
      const response = await this.profileService.addSeasonToRankingList(data);

      if (response.ok) {
        const newRanking: SeasonRankingData = await response.json();
        this.message = `Added ${this.selectedShowTitle} Season ${this.selectedSeason} to your ranking list!`;
        this.messageColor = "green";
        this.rankingEntries.push(newRanking);
      } else {
        this.message = `You already have ${this.selectedShowTitle} Season ${this.selectedSeason} on your ranking list.`;
        this.messageColor = "red";
      }
    } catch (error) {
      console.error(error);
    } finally {
      setTimeout(() => {
        this.message = "";
      }, 3000);
    }
  }
}
