import {Component, OnInit} from '@angular/core';
import {NgOptimizedImage} from "@angular/common";
import {SearchShowsModalComponent} from '../../../components/search-shows-modal/search-shows-modal.component';
import {SeasonSelectModalComponent} from '../../../components/season-select-modal/season-select-modal.component';
import {SeasonRankingData} from '../../../data/lists/season-ranking-data';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {ProfileService} from '../../../services/profile.service';
import {
  RankedSeasonListFullComponent
} from '../../../components/ranked-season-list-full/ranked-season-list-full.component';
import {UpdateSeasonRankingDto} from '../../../data/dto/update-list-ranks-dto';
import {SearchResultData} from '../../../data/search-result-data';
import {RouterLink} from '@angular/router';

@Component({
  selector: 'app-profile-edit-season-ranking-page',
  imports: [NgOptimizedImage, RankedSeasonListFullComponent, RouterLink],
  templateUrl: './profile-edit-season-ranking-page.component.html',
  styleUrl: './profile-edit-season-ranking-page.component.css',
})
export class ProfileEditSeasonRankingPageComponent implements OnInit {
  rankingEntries: SeasonRankingData[] = [];
  loadingData: boolean = true;
  selectedSeason: number = 1;
  selectedShow: SearchResultData | null = null;

  constructor(private modalService: NgbModal,
              private profileService: ProfileService) {}

  async ngOnInit() {
    try {
      this.rankingEntries = await this.profileService.getSeasonRankingList();
    } catch (error) {
      console.error(error);
    } finally {
      this.loadingData = false;
    }
  }

  async openSearchShowsModal() {
    const searchModalRef = this.modalService.open(SearchShowsModalComponent, {
      ariaLabelledBy: 'searchShowsModal',
      centered: true,
    });
    searchModalRef.componentInstance.modalTitle = 'Add Season to Ranking List';

    this.selectedShow = await searchModalRef.result;
    await this.openSeasonSelectModal();
  }

  async openSeasonSelectModal() {
    try {
      // Open season select modal and send required data
      const seasonModalRef = this.modalService.open(SeasonSelectModalComponent, {
        ariaLabelledBy: 'seasonSelectModal',
        centered: true
      });
      seasonModalRef.componentInstance.selectedShowId = this.selectedShow.id;
      seasonModalRef.componentInstance.selectedShowTitle = this.selectedShow.title;
      seasonModalRef.componentInstance.seasonRanking = true;
      seasonModalRef.componentInstance.onAddSeason = (season: SeasonRankingData) => {
        season.rankNum = this.rankingEntries.length + 1;
        this.rankingEntries.push(season);
      };

      const seasonResult = await seasonModalRef.result;
      this.selectedSeason = seasonResult.selectedSeason;
    } catch (modalDismissReason) {
      if (modalDismissReason === 'backFromSeason') {
        await this.openSearchShowsModal();
      }
    }
  }

  async removeSeasonFromRankingList(removeId: number) {
    try {
      await this.profileService.removeSeasonFromRankingList(removeId);

      // Remove the season from entries shown to user
      this.rankingEntries = this.rankingEntries.filter((season) => season.id != removeId);
    } catch (error) {
      console.error(error);
    }
  }

  async updateSeasonRankingList() {
    try {
      const updates: UpdateSeasonRankingDto[] = this.rankingEntries.map((season) => ({
        id: season.id,
        rankNum: season.rankNum
      }));

      await this.profileService.updateSeasonRankingList(updates);
    } catch (error) {
      console.error(error);
    }
  }
}
