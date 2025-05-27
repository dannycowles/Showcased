import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {UtilsService} from '../../services/utils.service';
import {ShowService} from '../../services/show.service';
import {TrendingShowsData} from '../../data/trending-shows-data';
import {SearchResultData} from '../../data/search-result-data';

@Component({
  selector: 'app-add-show-modal',
  imports: [
    FormsModule
  ],
  templateUrl: './add-show-modal.component.html',
  styleUrl: './add-show-modal.component.css',
  standalone: true
})
export class AddShowModalComponent implements OnInit {
  @Input({ required: true }) title: string;
  @Input() message: string;
  @Input() messageColor: string;
  @Output() addShow = new EventEmitter<SearchResultData>();

  searchString: string;
  selectedShowId: number;
  debouncedSearchShows: () => void;
  showSearchResults: TrendingShowsData;

  constructor(private showService: ShowService,
              private utils: UtilsService) { };

  ngOnInit() {
    this.debouncedSearchShows = this.utils.debounce(() => {
      this.searchShows();
    });
  }

  async searchShows() {
    try {
      this.showSearchResults = await this.showService.searchForShows(this.searchString);
    } catch (error) {
      console.error(error);
    }
  }

  addShowEvent() {
    const show = this.showSearchResults.results.find(show => show.id === this.selectedShowId);
    this.addShow.emit(show);
  }
}
