import {Component, OnInit} from '@angular/core';
import {SearchShowsModalComponent} from '../../../components/search-shows-modal/search-shows-modal.component';
import {AddShowType} from '../../../data/enums';
import {ShowListData} from '../../../data/lists/show-list-data';
import {ProfileService} from '../../../services/profile.service';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {NgOptimizedImage} from '@angular/common';
import {RouterLink} from '@angular/router';

@Component({
  selector: 'app-profile-edit-watching-page',
  imports: [NgOptimizedImage, RouterLink],
  templateUrl: './profile-edit-watching-page.component.html',
  styleUrl: './profile-edit-watching-page.component.css',
})
export class ProfileEditWatchingPageComponent implements OnInit {
  watchingEntries: ShowListData[];

  constructor(private profileService: ProfileService,
              private modalService: NgbModal) {}

  async ngOnInit() {
    try {
      this.watchingEntries = await this.profileService.getFullWatchingList();
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
    searchModalRef.componentInstance.modalTitle = 'Add Show to Watching List';
    searchModalRef.componentInstance.addType = AddShowType.Watching;
    searchModalRef.componentInstance.onAddShow = (show: ShowListData) => {
      this.watchingEntries.push(show);
    };
  }

  async removeFromWatchingList(removeId: number) {
    try {
      await this.profileService.removeShowFromWatchingList(removeId);

      // Remove the show from the entries shown to the user
      this.watchingEntries = this.watchingEntries.filter((show) => show.showId != removeId);
    } catch (error) {
      console.error(error);
    }
  }

  async moveToRankingList(moveId: number) {
    try {
      await this.profileService.moveShowToRankingList(moveId);

      // Remove the show from the entries shown to the user
      this.watchingEntries = this.watchingEntries.filter((show) => show.showId != moveId);
    } catch (error) {
      console.error(error);
    }
  }
}
