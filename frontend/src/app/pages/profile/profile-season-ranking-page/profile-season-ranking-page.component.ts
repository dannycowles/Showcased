import {Component, OnInit} from '@angular/core';
import {ProfileService} from '../../../services/profile.service';
import {SeasonRankingData} from '../../../data/lists/season-ranking-data';
import {SearchShowsModalComponent} from '../../../components/search-shows-modal/search-shows-modal.component';
import {SeasonSelectModalComponent} from '../../../components/season-select-modal/season-select-modal.component';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {SearchResultData} from '../../../data/search-result-data';

@Component({
  selector: 'app-profile-season-ranking-page',
  templateUrl: './profile-season-ranking-page.component.html',
  styleUrl: './profile-season-ranking-page.component.css',
  standalone: false
})
export class ProfileSeasonRankingPageComponent implements OnInit{
  rankingEntries: SeasonRankingData[];
  selectedSeason: number = 1;
  selectedShow: SearchResultData | null = null;

  constructor(private profileService: ProfileService,
              private modalService: NgbModal) { };

  async ngOnInit() {
    try {
      this.rankingEntries = await this.profileService.getSeasonRankingList();
    } catch (error) {
      console.error(error);
    }
  };

  async openSearchShowsModal() {
    const searchModalRef = this.modalService.open(SearchShowsModalComponent, {
      ariaLabelledBy: "searchShowsModal",
      centered: true
    });
    searchModalRef.componentInstance.modalTitle = "Add Season to Ranking List";

    this.selectedShow = await searchModalRef.result;
    await this.openSeasonSelectModal();
  }

  async openSeasonSelectModal() {
    try {
      // Open season select modal and send required data
      const seasonModalRef = this.modalService.open(SeasonSelectModalComponent, {
        ariaLabelledBy: "seasonSelectModal",
        centered: true
      });
      seasonModalRef.componentInstance.selectedShowId = this.selectedShow.id;
      seasonModalRef.componentInstance.selectedShowTitle = this.selectedShow.title;
      seasonModalRef.componentInstance.seasonRanking = true;
      seasonModalRef.componentInstance.onAddSeason = (season: SeasonRankingData) => {
        season.rankNum = this.rankingEntries.length + 1;
        this.rankingEntries.push(season);
      }

      const seasonResult = await seasonModalRef.result;
      this.selectedSeason = seasonResult.selectedSeason;
    } catch (modalDismissReason) {
      if (modalDismissReason === "backFromSeason"){
        await this.openSearchShowsModal();
      }
    }
  }

  async removeSeasonFromRankingList(removeId: number) {
    try {
      await this.profileService.removeSeasonFromRankingList(removeId);

      // Remove the season from entries shown to user
      this.rankingEntries = this.rankingEntries.filter(season => season.id != removeId);
    } catch(error) {
      console.error(error);
    }
  }

  async updateSeasonRankingList() {
    try {
      let updates = this.rankingEntries.map(season => ({
        "id": season.id,
        "rankNum": season.rankNum
      }));
      await this.profileService.updateSeasonRankingList(updates);
    } catch(error) {
      console.error(error);
    }
  }
}
