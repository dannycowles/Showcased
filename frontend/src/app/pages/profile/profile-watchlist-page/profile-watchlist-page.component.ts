import {Component, OnInit} from '@angular/core';
import {ProfileService} from '../../../services/profile.service';
import {ShowListData} from '../../../data/lists/show-list-data';
import {SearchShowsModalComponent} from '../../../components/search-shows-modal/search-shows-modal.component';
import {AddShowType} from '../../../data/enums';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {ShowListFullComponent} from '../../../components/show-list-full/show-list-full.component';
import {RouterLink} from '@angular/router';

@Component({
  selector: 'app-profile-watchlist-page',
  templateUrl: './profile-watchlist-page.component.html',
  styleUrl: './profile-watchlist-page.component.css',
  imports: [ShowListFullComponent, RouterLink],
  standalone: true,
})
export class ProfileWatchlistPageComponent implements OnInit {
  watchlistEntries: ShowListData[];

  constructor(
    private profileService: ProfileService,
    private modalService: NgbModal,
  ) {}

  async ngOnInit() {
    // Retrieve all watchlist entries for profile
    try {
      this.watchlistEntries = await this.profileService.getFullWatchlist();
    } catch (error) {
      console.error(error);
    }
  }

  openSearchShowsModal() {
    const searchModalRef = this.modalService.open(SearchShowsModalComponent, {
      ariaLabelledBy: 'searchShowsModal',
      centered: true,
    });
    searchModalRef.componentInstance.showRanking = true;
    searchModalRef.componentInstance.modalTitle = 'Add Show to Watchlist';
    searchModalRef.componentInstance.addType = AddShowType.Watchlist;
    searchModalRef.componentInstance.onAddShow = (show: ShowListData) => {
      this.watchlistEntries.push(show);
    };
  }

  async removeShowFromWatchlist(removeId: number) {
    try {
      await this.profileService.removeShowFromWatchlist(removeId);

      // Remove the show from the entries shown to the user
      this.watchlistEntries = this.watchlistEntries.filter(
        (show) => show.showId != removeId,
      );
    } catch (error) {
      console.error(error);
    }
  }

  async moveShowToWatchingList(moveId: number) {
    try {
      await this.profileService.moveShowToWatchingList(moveId);

      // Remove the show from the entries shown to the user
      this.watchlistEntries = this.watchlistEntries.filter(
        (show) => show.showId != moveId,
      );
    } catch (error) {
      console.error(error);
    }
  }
}
