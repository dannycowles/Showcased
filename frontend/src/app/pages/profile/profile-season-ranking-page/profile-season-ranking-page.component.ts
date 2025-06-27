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

  hasSearched: boolean = false;
  isLoading: boolean = false;

  debouncedSearchSeasons: () => void;

  constructor(private profileService: ProfileService,
              private utilsService: UtilsService,
              private showService: ShowService) { };

  async ngOnInit() {
    try {
      this.rankingEntries = await this.profileService.getSeasonRankingList();
    } catch (error) {
      console.error(error);
    }

    this.debouncedSearchSeasons = this.utilsService.debounce(() => {
      this.searchShows();
    })
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
    if (this.selectedShowId == null) return "";
    return this.showSearchResults.results.find(show => show.id == this.selectedShowId).title;
  }
}
