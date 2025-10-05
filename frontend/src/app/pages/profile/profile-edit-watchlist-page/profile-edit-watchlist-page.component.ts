import {Component, OnInit} from '@angular/core';
import {NgOptimizedImage} from '@angular/common';
import {RouterLink} from '@angular/router';
import {ProfileService} from '../../../services/profile.service';
import {ShowListData} from '../../../data/lists/show-list-data';
import {SearchShowsModalComponent} from '../../../components/search-shows-modal/search-shows-modal.component';
import {AddShowType} from '../../../data/enums';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-profile-edit-watchlist-page',
  imports: [NgOptimizedImage, RouterLink],
  templateUrl: './profile-edit-watchlist-page.component.html',
  styleUrl: './profile-edit-watchlist-page.component.css',
  standalone: true,
})
export class ProfileEditWatchlistPageComponent implements OnInit {
  watchlistEntries: ShowListData[];
  loadingData: boolean = true;

  constructor(private profileService: ProfileService,
              private modalService: NgbModal) {}

  async ngOnInit() {
    try {
      this.watchlistEntries = await this.profileService.getFullWatchlist();
    } catch (error) {
      console.error(error);
    } finally {
      this.loadingData = false;
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

  async removeFromWatchlist(removeId: number) {
    try {
      await this.profileService.removeShowFromWatchlist(removeId);

      // Remove the show from the entries shown to the user
      this.watchlistEntries = this.watchlistEntries.filter((show) => show.showId != removeId);
    } catch (error) {
      console.error(error);
    }
  }

  async moveToWatchingList(moveId: number) {
    try {
      await this.profileService.moveShowToWatchingList(moveId);

      // Remove the show from the entries shown to the user
      this.watchlistEntries = this.watchlistEntries.filter((show) => show.showId != moveId);
    } catch (error) {
      console.error(error);
    }
  }
}
