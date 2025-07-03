import {Component, Input, OnInit} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';
import {ShowService} from '../../services/show.service';
import {RoleData} from '../../data/role-data';
import {UtilsService} from '../../services/utils.service';
import {SearchResultData} from '../../data/search-result-data';
import {ProfileService} from '../../services/profile.service';
import {CharacterRankingData} from '../../data/lists/character-ranking-data';

@Component({
  selector: 'app-search-characters-modal',
  imports: [FormsModule],
  templateUrl: './search-characters-modal.component.html',
  styleUrl: './search-characters-modal.component.css',
  standalone: true
})
export class SearchCharactersModalComponent implements OnInit {
  @Input({required: true}) selectedShow: SearchResultData;
  @Input({required: true}) selectedCharacterType: string;
  searchCharacterString: string = "";
  searchCharacterResults: RoleData[];
  selectedCharacter: RoleData;
  message: string = "";
  messageColor: string = "";
  isLoading: boolean = false;

  debouncedSearchCharacters: () => void;

  @Input({required: true}) onAdd: (character: {}) => void;

  constructor(public activeModal: NgbActiveModal,
              private showService: ShowService,
              private utilsService: UtilsService,
              private profileService: ProfileService) {
    this.debouncedSearchCharacters = this.utilsService.debounce(() => this.searchCharacters());
  };

  async ngOnInit() {
    await this.searchCharacters();
  }

  goBack() {
    this.activeModal.dismiss("backFromCharacters");
  }

  async searchCharacters() {
    this.isLoading = true;

    try {
      this.searchCharacterResults = await this.showService.searchCharacters(this.selectedShow.id, this.searchCharacterString);
    } catch (error) {
      console.error(error);
    } finally {
      this.isLoading = false;
    }
  }

  async addCharacter() {
    try {
      const data = {
        id: this.selectedCharacter.id,
        showId: this.selectedShow.id,
        name: this.selectedCharacter.name,
        type: this.selectedCharacterType !== "side" ? this.selectedCharacterType.slice(0, -1) : this.selectedCharacterType,
        title: this.selectedShow.title,
        posterPath: this.selectedShow.posterPath
      }

      const response = await this.profileService.addCharacterToRankingList(data);
      if (response.ok) {
        this.message = `Added ${this.selectedCharacter.name} to your ranking list!`;
        this.messageColor = "green";

        const characterData: CharacterRankingData = {
          id: this.selectedCharacter.id,
          showId: this.selectedShow.id,
          name: this.selectedCharacter.name,
          showName: this.selectedShow.title,
          rankNum: null
        }
        this.onAdd(characterData);
      } else {
        this.message = `You already have ${this.selectedCharacter.name} on your ranking list.`
        this.messageColor = "red";
      }
    } catch (error) {
      console.error(error);
    } finally {
      setTimeout(() => this.message = "", 3000);
    }
  }
}
