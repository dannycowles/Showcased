import {Component, Input} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {ResultPageData} from '../../data/show/result-page-data';
import {NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';
import {UtilsService} from '../../services/utils.service';
import {ShowService} from '../../services/show.service';
import {ProfileService} from '../../services/profile.service';
import {SearchResultData} from '../../data/search-result-data';
import {AddShowType} from '../../data/enums';

@Component({
  selector: 'app-search-shows-modal',
  imports: [FormsModule],
  templateUrl: './search-shows-modal.component.html',
  styleUrl: './search-shows-modal.component.css',
  standalone: true,
})
export class SearchShowsModalComponent {
  @Input({required: true}) modalTitle: string;
  @Input({required: true}) addType: AddShowType;
  searchString: string = "";
  showSearchResults: ResultPageData | null = null;
  selectedShowId: number | null = null;

  hasSearched: boolean = false;
  isLoading: boolean = false;

  // These variables are used when the component calling this modal is adding shows to ranking list
  @Input() showRanking: boolean = false;
  @Input() onAddShow: (show: {}) => void;
  message: string;
  messageColor: string;

  // Used if the component calling the modal is adding the show to a collection
  @Input() collectionId: number;

  debouncedSearchShows: () => void;

  constructor (public activeModal: NgbActiveModal,
               private utilsService: UtilsService,
               private showService: ShowService,
               private profileService: ProfileService) {
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

  get selectedShow(): SearchResultData {
    if (this.selectedShowId == null || this.showSearchResults == null) return null;
    return this.showSearchResults.results.find(show => show.id === this.selectedShowId);
  }

  showYears(show: SearchResultData): string {
    if (show.startYear === show.endYear) {
      return `(${show.startYear})`;
    } else if (show.endYear == null) {
      return `(${show.startYear} - )`;
    } else {
      return `(${show.startYear} - ${show.endYear})`;
    }
  }

  passBack() {
    this.activeModal.close(this.selectedShow);
  }

  async addShow() {
    try {
      const selectedShow = this.selectedShow;
      const showData = {
        showId: this.selectedShowId,
        showTitle: selectedShow.title,
        posterPath: selectedShow.posterPath
      };

      let response;
      switch (this.addType) {
        case AddShowType.Ranking: {
          response = await this.profileService.addShowToRankingList(showData);
          break;
        }
        case AddShowType.Watching: {
          response = await this.profileService.addShowToWatchingList(showData);
          break;
        }
        case AddShowType.Watchlist: {
          response = await this.profileService.addShowToWatchlist(showData);
          break;
        }
        case AddShowType.Collection: {
          response = await this.profileService.addShowToCollection(this.collectionId, showData);
          break;
        }
      }

      if (response.ok) {
        this.message = `Added ${selectedShow.title} to your ${this.addType}!`;
        this.messageColor = 'green';
        this.onAddShow(showData);
      } else if (response.status === 409) {
        this.message = `You already have ${selectedShow.title} on your ${this.addType}.`;
        this.messageColor = 'red';
      }
    } catch (error) {
      console.error(error);
    } finally {
      setTimeout(() => this.message = "", 3000);
    }
  }
}
