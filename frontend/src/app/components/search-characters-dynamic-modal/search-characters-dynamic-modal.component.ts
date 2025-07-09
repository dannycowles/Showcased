import {Component, Input, OnInit} from '@angular/core';
import {SearchResultData} from '../../data/search-result-data';
import {ShowService} from '../../services/show.service';
import {NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';
import {FormsModule} from '@angular/forms';
import {RoleData} from '../../data/role-data';
import {UtilsService} from '../../services/utils.service';
import {ProfileService} from '../../services/profile.service';
import {DynamicRankingData} from '../../data/lists/dynamic-ranking-data';

@Component({
  selector: 'app-search-characters-dynamic-modal',
  imports: [FormsModule],
  templateUrl: './search-characters-dynamic-modal.component.html',
  styleUrl: './search-characters-dynamic-modal.component.css',
  standalone: true,
})
export class SearchCharactersDynamicModalComponent implements OnInit {
  @Input({ required: true }) selectedShow: SearchResultData;
  @Input({ required: true }) selectedCharacter1: RoleData;
  selectedCharacter2: RoleData | null = null;
  message: string = '';
  messageColor: string = '';
  searchCharacterString: string = '';
  searchCharacterResults: RoleData[];
  isLoading: boolean = false;

  debouncedSearchCharacters: () => void;

  @Input({ required: true}) onAdd: (dynamic: {}) => void;

  constructor(private showService: ShowService,
              public activeModal: NgbActiveModal,
              public utilsService: UtilsService,
              private profileService: ProfileService) {
    this.debouncedSearchCharacters = this.utilsService.debounce(() => this.searchCharacters());
  };

  async ngOnInit() {
    await this.searchCharacters();
  }

  goBack() {
    this.activeModal.dismiss('backFromCharacters2');
  }

  async searchCharacters() {
    this.isLoading = true;

    try {
      this.searchCharacterResults = await this.showService.searchCharacters(this.selectedShow.id, this.searchCharacterString)
    } catch (error) {
      console.error(error);
    } finally {
      this.isLoading = false;
    }
  }

  async addDynamic() {
    try {
      // Ensure both characters are not the same
      if (this.selectedCharacter1.id === this.selectedCharacter2.id) {
        this.message = "Characters cannot be the same!";
        this.messageColor = "red";
      }

      const data = {
        character1Id: this.selectedCharacter1.id,
        character1Name: this.selectedCharacter1.name,
        character2Id: this.selectedCharacter2.id,
        character2Name: this.selectedCharacter2.name,
        showId: this.selectedShow.id,
        showTitle: this.selectedShow.title,
        posterPath: this.selectedShow.posterPath
      }

      const response = await this.profileService.addDynamicToRankingList(data);
      if (response.ok) {
        this.message = "Added dynamic to ranking list!";
        this.messageColor = "green";
        const newDynamic: DynamicRankingData = await response.json();
        this.onAdd(newDynamic);
      } else if (response.status === 409) {
        this.message = "You already have this dynamic on your ranking list.";
        this.messageColor = "red";
      }
    } catch (error) {
      console.error(error);
    } finally {
      setTimeout(() => this.message = "", 3000);
    }
  }

}
