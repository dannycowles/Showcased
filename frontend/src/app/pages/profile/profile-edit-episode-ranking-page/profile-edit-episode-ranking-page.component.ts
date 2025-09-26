import {Component, OnInit} from '@angular/core';
import {NgOptimizedImage} from '@angular/common';
import {RouterLink} from '@angular/router';
import {SearchShowsModalComponent} from '../../../components/search-shows-modal/search-shows-modal.component';
import {SeasonSelectModalComponent} from '../../../components/season-select-modal/season-select-modal.component';
import {EpisodeSelectModalComponent} from '../../../components/episode-select-modal/episode-select-modal.component';
import {EpisodeRankingData} from '../../../data/lists/episode-ranking-data';
import {UpdateEpisodeRankingDto} from '../../../data/dto/update-list-ranks-dto';
import {SearchResultData} from '../../../data/search-result-data';
import {ProfileService} from '../../../services/profile.service';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {
  RankedEpisodeListFullComponent
} from '../../../components/ranked-episode-list-full/ranked-episode-list-full.component';

@Component({
  selector: 'app-profile-edit-episode-ranking-page',
  imports: [NgOptimizedImage, RouterLink, RankedEpisodeListFullComponent],
  templateUrl: './profile-edit-episode-ranking-page.component.html',
  styleUrl: './profile-edit-episode-ranking-page.component.css',
  standalone: true,
})
export class ProfileEditEpisodeRankingPageComponent implements OnInit {
  rankingEntries: EpisodeRankingData[];
  selectedSeason: number = 1;
  selectedShow: SearchResultData | null = null;

  constructor(
    private profileService: ProfileService,
    private modalService: NgbModal,
  ) {}

  async ngOnInit() {
    try {
      this.rankingEntries =
        await this.profileService.getFullEpisodeRankingList();
    } catch (error) {
      console.error(error);
    }
  }

  async openSearchShowsModal() {
    const searchModalRef = this.modalService.open(SearchShowsModalComponent, {
      ariaLabelledBy: 'searchShowsModal',
      centered: true,
    });
    searchModalRef.componentInstance.modalTitle = 'Add Episode to Ranking List';

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

      const seasonResult = await seasonModalRef.result;
      this.selectedSeason = seasonResult.selectedSeason;

      await this.openEpisodeSelectModal();
    } catch (modalDismissReason) {
      if (modalDismissReason === 'backFromSeason') {
        await this.openSearchShowsModal();
      }
    }
  }

  async openEpisodeSelectModal() {
    try {
      // Open episode select modal and send required data
      const episodeModalRef = this.modalService.open(EpisodeSelectModalComponent, {
        ariaLabelledBy: 'episodeSelectModal',
        centered: true
      });
      episodeModalRef.componentInstance.selectedShowId = this.selectedShow.id;
      episodeModalRef.componentInstance.selectedShowTitle = this.selectedShow.title;
      episodeModalRef.componentInstance.selectedSeason = this.selectedSeason;
      episodeModalRef.componentInstance.onAddEpisode = (episode: EpisodeRankingData) => {
        episode.rankNum = this.rankingEntries.length + 1;
        this.rankingEntries.push(episode);
      };

      await episodeModalRef.result;
    } catch (modalDismissReason) {
      if (modalDismissReason === 'backFromEpisode') {
        await this.openSeasonSelectModal();
      }
    }
  }

  async removeEpisodeFromRankingList(removeId: number) {
    try {
      await this.profileService.removeEpisodeFromRankingList(removeId);

      // Remove the episode from entries shown to the user
      this.rankingEntries = this.rankingEntries.filter((episode) => episode.episodeId !== removeId);
    } catch (error) {
      console.error(error);
    }
  }

  async updateEpisodeRankingList() {
    try {
      const updates: UpdateEpisodeRankingDto[] = this.rankingEntries.map((episode) => ({
        id: episode.episodeId,
        rankNum: episode.rankNum
      }));

      await this.profileService.updateEpisodeRankingList(updates);
    } catch (error) {
      console.error(error);
    }
  }
}
