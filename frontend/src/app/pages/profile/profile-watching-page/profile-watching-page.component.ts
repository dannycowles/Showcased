import {Component, OnInit} from '@angular/core';
import {ProfileService} from '../../../services/profile.service';
import {ShowListData} from '../../../data/lists/show-list-data';
import {SearchShowsModalComponent} from '../../../components/search-shows-modal/search-shows-modal.component';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {AddShowType} from '../../../data/enums';

@Component({
  selector: 'app-profile-watching-page',
  templateUrl: './profile-watching-page.component.html',
  styleUrl: './profile-watching-page.component.css',
  standalone: false
})
export class ProfileWatchingPageComponent implements OnInit {
  watchingEntries: ShowListData[];

  constructor(private profileService: ProfileService,
              private modalService: NgbModal) { }

  async ngOnInit() {

    // Retrieve watching entries for profile
    try {
      this.watchingEntries = await this.profileService.getFullWatchingList();
    } catch(error) {
      console.error(error);
    }
  }

  async openSearchShowsModal() {
    const searchModalRef = this.modalService.open(SearchShowsModalComponent, {
      ariaLabelledBy: "searchShowsModal",
      centered: true
    });
    searchModalRef.componentInstance.showRanking = true;
    searchModalRef.componentInstance.modalTitle = "Add Show to Watching List";
    searchModalRef.componentInstance.addType = AddShowType.Watching;
    searchModalRef.componentInstance.onAddShow = (show: ShowListData) => {
      this.watchingEntries.push(show);
    }
  }

  async removeShowFromWatchingList(removeId: number) {
    try {
      await this.profileService.removeShowFromWatchingList(removeId);

      // Remove the show from the entries shown to the user
      this.watchingEntries = this.watchingEntries.filter(show => show.showId != removeId);
    } catch(error) {
      console.error(error);
    }
  }

  async moveShowToRankingList(moveId: number) {
    try {
      await this.profileService.moveShowToRankingList(moveId);

      // Remove the show from the entries shown to the user
      this.watchingEntries = this.watchingEntries.filter(show => show.showId != moveId);
    } catch (error) {
      console.error(error);
    }
  }
}
