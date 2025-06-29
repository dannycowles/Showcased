import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {ProfileService} from '../../../services/profile.service';
import {EpisodeRankingData} from '../../../data/lists/episode-ranking-data';
import {ResultPageData} from '../../../data/show/result-page-data';
import {UtilsService} from '../../../services/utils.service';
import {ShowService} from '../../../services/show.service';
import * as bootstrap from 'bootstrap';

@Component({
  selector: 'app-profile-episode-ranking-page',
  templateUrl: './profile-episode-ranking-page.component.html',
  styleUrl: './profile-episode-ranking-page.component.css',
  standalone: false
})
export class ProfileEpisodeRankingPageComponent implements OnInit {
  rankingEntries: EpisodeRankingData[];
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

  @ViewChild("searchShowsModal") searchShowsModal!: ElementRef<HTMLElement>;
  @ViewChild("seasonSelectModal") seasonSelectModal!: ElementRef<HTMLElement>;
  @ViewChild("episodeSelectModal") episodeSelectModal!: ElementRef<HTMLElement>;

  constructor(private profileService: ProfileService,
              private utilsService: UtilsService,
              private showService: ShowService) { }

  async ngOnInit() {
    // Retrieve episode ranking entries for the profile
    try {
      this.rankingEntries = await this.profileService.getFullEpisodeRankingList();
    } catch(error) {
      console.error(error);
    }

    this.debouncedSearchSeasons = this.utilsService.debounce(() => this.searchShows());
  }

  openSearchShowsModal() {
    // Reset fields and open modal
    this.searchString = "";
    this.showSearchResults = null;
    this.hasSearched = false;

    bootstrap.Modal.getOrCreateInstance(this.searchShowsModal.nativeElement).show();
  }

  openSeasonSelectModal() {
    this.getNumberOfSeasons();
    bootstrap.Modal.getInstance(this.searchShowsModal.nativeElement).hide();
    bootstrap.Modal.getOrCreateInstance(this.seasonSelectModal.nativeElement).show();
  }

  backToSearchShowsModal() {
    bootstrap.Modal.getInstance(this.seasonSelectModal.nativeElement).hide();
    bootstrap.Modal.getInstance(this.searchShowsModal.nativeElement).show();
  }

  openEpisodeSelectModal() {
    bootstrap.Modal.getInstance(this.seasonSelectModal.nativeElement).hide();
    bootstrap.Modal.getOrCreateInstance(this.episodeSelectModal.nativeElement).show();
  }

  backToSeasonSelectModal() {
    bootstrap.Modal.getInstance(this.episodeSelectModal.nativeElement).hide();
    bootstrap.Modal.getInstance(this.seasonSelectModal.nativeElement).show();
  }

  async removeEpisodeFromRankingList(removeId: number) {
    try {
      await this.profileService.removeEpisodeFromRankingList(removeId);

      // Remove the episode from entries shown to the user
      this.rankingEntries = this.rankingEntries.filter(show => show.id !== removeId);
    } catch(error) {
      console.error(error);
    }
  }

  async updateEpisodeRankingList() {
    try {
      const updates = this.rankingEntries.map(episode => ({
        "episodeId": episode.id,
        "rankNum": episode.rankNum
      }));
      await this.profileService.updateEpisodeRankingList(updates);
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
}
