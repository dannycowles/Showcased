import {Component, OnInit} from '@angular/core';
import {ShowRankingData} from '../../../data/lists/show-ranking-data';
import {ProfileService} from '../../../services/profile.service';
import {RankedShowListFullComponent} from '../../../components/ranked-show-list-full/ranked-show-list-full.component';
import {UpdateShowRankingDto} from '../../../data/dto/update-list-ranks-dto';
import {SearchShowsModalComponent} from '../../../components/search-shows-modal/search-shows-modal.component';
import {AddShowType} from '../../../data/enums';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {NgOptimizedImage} from '@angular/common';
import {RouterLink} from '@angular/router';

@Component({
  selector: 'app-profile-edit-show-ranking-page',
  imports: [RankedShowListFullComponent, NgOptimizedImage, RouterLink],
  templateUrl: './profile-edit-show-ranking-page.component.html',
  styleUrl: './profile-edit-show-ranking-page.component.css',
  standalone: true,
})
export class ProfileEditShowRankingPageComponent implements OnInit {
  rankingEntries: ShowRankingData[] = [];
  loadingData: boolean = true;

  constructor(
    private profileService: ProfileService,
    private modalService: NgbModal,
  ) {}

  async ngOnInit() {
    try {
      this.rankingEntries = await this.profileService.getFullShowRankingList();
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
    searchModalRef.componentInstance.modalTitle = 'Add Show to Ranking List';
    searchModalRef.componentInstance.addType = AddShowType.Ranking;
    searchModalRef.componentInstance.onAddShow = (show: ShowRankingData) => {
      show.rankNum = this.rankingEntries.length + 1;
      this.rankingEntries.push(show);
    };
  }

  async removeShowFromRankingList(removeId: number) {
    try {
      await this.profileService.removeShowFromRankingList(removeId);

      // Remove the show from entries shown to user
      this.rankingEntries = this.rankingEntries.filter((show) => show.showId != removeId);
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
