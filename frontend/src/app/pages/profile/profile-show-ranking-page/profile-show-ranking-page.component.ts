import { Component, OnInit } from '@angular/core';
import { ProfileService } from '../../../services/profile.service';
import { ShowRankingData } from '../../../data/lists/show-ranking-data';
import {SearchShowsModalComponent} from '../../../components/search-shows-modal/search-shows-modal.component';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {AddShowType} from '../../../data/enums';
import {UpdateShowRankingDto} from '../../../data/dto/update-list-ranks-dto';

@Component({
  selector: 'app-profile-show-ranking-page',
  templateUrl: './profile-show-ranking-page.component.html',
  styleUrl: './profile-show-ranking-page.component.css',
  standalone: false,
})
export class ProfileShowRankingPageComponent implements OnInit {
  rankingEntries: ShowRankingData[];

  constructor(private profileService: ProfileService,
              private modalService: NgbModal) {}

  async ngOnInit() {
    // Retrieve the show ranking entries for the profile
    try {
      this.rankingEntries = await this.profileService.getFullShowRankingList();
    } catch (error) {
      console.error(error);
    }
  }

  openSearchShowsModal() {
    const searchModalRef = this.modalService.open(SearchShowsModalComponent, {
      ariaLabelledBy: "searchShowsModal",
      centered: true
    });
    searchModalRef.componentInstance.showRanking = true;
    searchModalRef.componentInstance.modalTitle = "Add Show to Ranking List";
    searchModalRef.componentInstance.addType = AddShowType.Ranking;
    searchModalRef.componentInstance.onAddShow = (show: ShowRankingData) => {
      show.rankNum = this.rankingEntries.length + 1;
      this.rankingEntries.push(show);
    }
  }

  async removeShowFromRankingList(removeId: number) {
    try {
      await this.profileService.removeShowFromRankingList(removeId);

      // Remove the show from entries shown to user
      this.rankingEntries = this.rankingEntries.filter(
        (show) => show.showId != removeId,
      );
    } catch (error) {
      console.error(error);
    }
  }

  async updateShowRankingList() {
    try {
      const updates: UpdateShowRankingDto[] = this.rankingEntries.map((show) => ({
        id: show.showId,
        rankNum: show.rankNum,
      }));
      await this.profileService.updateShowRankingList(updates);
    } catch (error) {
      console.error(error);
    }
  }
}
