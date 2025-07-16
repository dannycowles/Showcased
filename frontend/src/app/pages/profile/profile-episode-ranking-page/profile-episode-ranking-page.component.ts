import {Component, OnInit} from '@angular/core';
import {ProfileService} from '../../../services/profile.service';
import {EpisodeRankingData} from '../../../data/lists/episode-ranking-data';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {SearchShowsModalComponent} from '../../../components/search-shows-modal/search-shows-modal.component';
import {SeasonSelectModalComponent} from '../../../components/season-select-modal/season-select-modal.component';
import {EpisodeSelectModalComponent} from '../../../components/episode-select-modal/episode-select-modal.component';
import {SearchResultData} from '../../../data/search-result-data';
import {UpdateEpisodeRankingDto} from '../../../data/dto/update-list-ranks-dto';

@Component({
  selector: 'app-profile-episode-ranking-page',
  templateUrl: './profile-episode-ranking-page.component.html',
  styleUrl: './profile-episode-ranking-page.component.css',
  standalone: false
})
export class ProfileEpisodeRankingPageComponent implements OnInit {
  rankingEntries: EpisodeRankingData[];
  selectedSeason: number = 1;
  selectedShow: SearchResultData | null = null;

  constructor(private profileService: ProfileService,
              private modalService: NgbModal) {};

  async ngOnInit() {
    // Retrieve episode ranking entries for the profile
    try {
      this.rankingEntries = await this.profileService.getFullEpisodeRankingList();
    } catch(error) {
      console.error(error);
    }
  }

  async openSearchShowsModal() {
    const searchModalRef = this.modalService.open(SearchShowsModalComponent, {
      ariaLabelledBy: "searchShowsModal",
      centered: true
    });
    searchModalRef.componentInstance.modalTitle = "Add Episode to Ranking List";

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

      const seasonResult = await seasonModalRef.result;
      this.selectedSeason = seasonResult.selectedSeason;

      await this.openEpisodeSelectModal();
    } catch (modalDismissReason) {
      if (modalDismissReason === "backFromSeason"){
        await this.openSearchShowsModal();
      }
    }
  }

  async openEpisodeSelectModal() {
    try {
      // Open episode select modal and send required data
      const episodeModalRef = this.modalService.open(EpisodeSelectModalComponent, {
        ariaLabelledBy: "episodeSelectModal",
        centered: true
      });
      episodeModalRef.componentInstance.selectedShowId = this.selectedShow.id;
      episodeModalRef.componentInstance.selectedShowTitle = this.selectedShow.title;
      episodeModalRef.componentInstance.selectedSeason = this.selectedSeason;
      episodeModalRef.componentInstance.onAddEpisode = (episode: EpisodeRankingData) => {
        episode.rankNum = this.rankingEntries.length + 1;
        this.rankingEntries.push(episode);
      }

      await episodeModalRef.result;
    } catch (modalDismissReason) {
      if (modalDismissReason === "backFromEpisode") {
        await this.openSeasonSelectModal();
      }
    }
  }

  async removeEpisodeFromRankingList(removeId: number) {
    try {
      await this.profileService.removeEpisodeFromRankingList(removeId);

      // Remove the episode from entries shown to the user
      this.rankingEntries = this.rankingEntries.filter(episode => episode.episodeId !== removeId);
    } catch(error) {
      console.error(error);
    }
  }

  async updateEpisodeRankingList() {
    try {
      const updates: UpdateEpisodeRankingDto[] = this.rankingEntries.map(episode => ({
        id: episode.episodeId,
        rankNum: episode.rankNum
      }));
      await this.profileService.updateEpisodeRankingList(updates);
    } catch(error) {
      console.error(error);
    }
  }
}
