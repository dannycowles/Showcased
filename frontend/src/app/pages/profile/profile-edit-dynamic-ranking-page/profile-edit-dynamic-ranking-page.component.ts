import {Component, OnInit} from '@angular/core';
import {NgOptimizedImage} from "@angular/common";
import {RouterLink} from '@angular/router';
import {DynamicRankingData} from '../../../data/lists/dynamic-ranking-data';
import {ProfileService} from '../../../services/profile.service';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {SearchShowsModalComponent} from '../../../components/search-shows-modal/search-shows-modal.component';
import {
  SearchCharactersModalComponent
} from '../../../components/search-characters-modal/search-characters-modal.component';
import {
  SearchCharactersDynamicModalComponent
} from '../../../components/search-characters-dynamic-modal/search-characters-dynamic-modal.component';
import {UpdateDynamicRankingDto} from '../../../data/dto/update-list-ranks-dto';
import {SearchResultData} from '../../../data/search-result-data';
import {RoleData} from '../../../data/role-data';
import {
  RankedDynamicListFullComponent
} from '../../../components/ranked-dynamic-list-full/ranked-dynamic-list-full.component';

@Component({
  selector: 'app-profile-edit-dynamic-ranking-page',
  imports: [
    NgOptimizedImage,
    RouterLink,
    RankedDynamicListFullComponent
  ],
  templateUrl: './profile-edit-dynamic-ranking-page.component.html',
  styleUrl: './profile-edit-dynamic-ranking-page.component.css'
})
export class ProfileEditDynamicRankingPageComponent implements OnInit {
  rankingEntries: DynamicRankingData[] = [];
  loadingData: boolean = true;
  selectedShow: SearchResultData | null = null;
  selectedCharacter1: RoleData | null = null;

  constructor(private profileService: ProfileService,
              private modalService: NgbModal) {};

  async ngOnInit() {
    try {
      this.rankingEntries = await this.profileService.getDynamicRankingList();
    } catch (error) {
      console.error(error);
    } finally {
      this.loadingData = false;
    }
  }

  async openSearchShowsModal() {
    const searchShowsModalRef = this.modalService.open(SearchShowsModalComponent, {
      ariaLabelledBy: 'searchShowsModal',
      centered: true
    });
    searchShowsModalRef.componentInstance.modalTitle = 'Add Character Dynamic to Ranking List';

    this.selectedShow = await searchShowsModalRef.result;
    await this.openSearchCharacter1Modal();
  }

  async openSearchCharacter1Modal() {
    try {
      const searchCharacter1ModalRef = this.modalService.open(SearchCharactersModalComponent, {
        ariaLabelledBy: 'searchCharactersModal',
        centered: true
      });
      searchCharacter1ModalRef.componentInstance.selectedShow = this.selectedShow;
      searchCharacter1ModalRef.componentInstance.dynamicSearch = true;

      this.selectedCharacter1 = await searchCharacter1ModalRef.result;
      await this.openSearchCharacter2Modal();
    } catch (modalDismissReason) {
      if (modalDismissReason === 'backFromCharacters') {
        await this.openSearchShowsModal();
      }
    }
  }

  async openSearchCharacter2Modal() {
    try {
      const searchCharacter2ModalRef = this.modalService.open(SearchCharactersDynamicModalComponent, {
        ariaLabelledBy: 'searchCharacterDynamicModal',
        centered: true
      });
      searchCharacter2ModalRef.componentInstance.selectedShow = this.selectedShow;
      searchCharacter2ModalRef.componentInstance.selectedCharacter1 = this.selectedCharacter1;
      searchCharacter2ModalRef.componentInstance.onAdd = (dynamic: DynamicRankingData) => {
        dynamic.rankNum = this.rankingEntries.length + 1;
        this.rankingEntries.push(dynamic);
      };

      await searchCharacter2ModalRef.result;
    } catch (modalDismissReason) {
      if (modalDismissReason === 'backFromCharacters2') {
        await this.openSearchCharacter1Modal();
      }
    }
  }

  async removeDynamicFromRankingList(removeId: number) {
    try {
      await this.profileService.removeDynamicFromRankingList(removeId);

      // Remove the dynamic from the list visible to the user
      this.rankingEntries = this.rankingEntries.filter((dynamic) => dynamic.id !== removeId);
    } catch (error) {
      console.error(error);
    }
  }

  async updateDynamicRankingList() {
    try {
      const updates: UpdateDynamicRankingDto[] = this.rankingEntries.map((dynamic) => ({
        id: dynamic.id,
        rankNum: dynamic.rankNum
      }));

      await this.profileService.updateDynamicRankingList(updates);
    } catch (error) {
      console.error(error);
    }
  }

}
