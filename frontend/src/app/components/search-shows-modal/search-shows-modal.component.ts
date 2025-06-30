import { Component } from '@angular/core';
import {FormsModule} from '@angular/forms';
import {EpisodeRankingData} from '../../data/lists/episode-ranking-data';
import {ResultPageData} from '../../data/show/result-page-data';
import {NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';
import {UtilsService} from '../../services/utils.service';
import {ShowService} from '../../services/show.service';

@Component({
  selector: 'app-search-shows-modal',
  imports: [FormsModule],
  templateUrl: './search-shows-modal.component.html',
  styleUrl: './search-shows-modal.component.css',
  standalone: true,
})
export class SearchShowsModalComponent {
  rankingEntries: EpisodeRankingData[];
  searchString: string = "";
  showSearchResults: ResultPageData | null = null;
  selectedShowId: number | null = null;

  hasSearched: boolean = false;
  isLoading: boolean = false;

  debouncedSearchShows: () => void;

  constructor (public activeModal: NgbActiveModal,
               private utilsService: UtilsService,
               private showService: ShowService) {
    this.debouncedSearchShows = this.utilsService.debounce(() => this.searchShows());
  };

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

  passBack() {
    this.activeModal.close({
      selectedShowId: this.selectedShowId,
      selectedShowTitle: this.selectedShowTitle
    });
  }
}
