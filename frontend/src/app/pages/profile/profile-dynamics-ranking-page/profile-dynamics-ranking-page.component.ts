import {Component, OnInit} from '@angular/core';
import {ProfileService} from '../../../services/profile.service';
import {DynamicRankingData} from '../../../data/lists/dynamic-ranking-data';
import {CdkDragDrop, moveItemInArray} from '@angular/cdk/drag-drop';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {SearchShowsModalComponent} from '../../../components/search-shows-modal/search-shows-modal.component';
import {SearchResultData} from '../../../data/search-result-data';
import {
  SearchCharactersModalComponent
} from '../../../components/search-characters-modal/search-characters-modal.component';
import {RoleData} from '../../../data/role-data';
import {
  SearchCharactersDynamicModalComponent
} from '../../../components/search-characters-dynamic-modal/search-characters-dynamic-modal.component';

@Component({
  selector: 'app-profile-dynamics-ranking-page',
  templateUrl: './profile-dynamics-ranking-page.component.html',
  styleUrl: './profile-dynamics-ranking-page.component.css',
  standalone: false,
})
export class ProfileDynamicsRankingPageComponent implements OnInit {
  dynamics: DynamicRankingData[];
  selectedShow: SearchResultData | null = null;
  selectedCharacter1: RoleData | null = null;

  constructor(private profileService: ProfileService,
              private modalService: NgbModal) {}

  async ngOnInit() {
    try {
      this.dynamics = await this.profileService.getDynamicRankingList();
    } catch (error) {
      console.error(error);
    }
  }

  drop(event: CdkDragDrop<DynamicRankingData[]>) {
    moveItemInArray(this.dynamics, event.previousIndex, event.currentIndex);
  }

  async openSearchShowsModal() {
    const searchShowsModalRef = this.modalService.open(SearchShowsModalComponent, {
      ariaLabelledBy: "searchShowsModal",
      centered: true
    });
    searchShowsModalRef.componentInstance.modalTitle = "Add Character Dynamic to Ranking List";

    this.selectedShow = await searchShowsModalRef.result;
    await this.openSearchCharacter1Modal();
  }

  async openSearchCharacter1Modal() {
    try {
      const searchCharacter1ModalRef = this.modalService.open(SearchCharactersModalComponent, {
        ariaLabelledBy: "searchCharactersModal",
        centered: true
      });
      searchCharacter1ModalRef.componentInstance.selectedShow = this.selectedShow;
      searchCharacter1ModalRef.componentInstance.dynamicSearch = true;

      this.selectedCharacter1 = await searchCharacter1ModalRef.result;
      await this.openSearchCharacter2Modal();
    } catch (modalDismissReason) {
      if (modalDismissReason === "backFromCharacters") {
        await this.openSearchShowsModal();
      }
    }
  }

  async openSearchCharacter2Modal() {
    try {
      const searchCharacter2ModalRef = this.modalService.open(SearchCharactersDynamicModalComponent, {
        ariaLabelledBy: "searchCharacterDynamicModal",
        centered: true
      });
      searchCharacter2ModalRef.componentInstance.selectedShow = this.selectedShow;
      searchCharacter2ModalRef.componentInstance.selectedCharacter1 = this.selectedCharacter1;

      await searchCharacter2ModalRef.result;
    } catch (modalDismissReason) {
      if (modalDismissReason === "backFromCharacters2") {
        await this.openSearchCharacter1Modal();
      }
    }
  }
}
