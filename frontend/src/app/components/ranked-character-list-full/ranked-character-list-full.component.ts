import {booleanAttribute, Component, Input} from '@angular/core';
import {ActivatedRoute, Router, RouterLink} from '@angular/router';
import {CdkDrag, CdkDragDrop, CdkDropList, moveItemInArray} from '@angular/cdk/drag-drop';
import {SearchShowsModalComponent} from '../search-shows-modal/search-shows-modal.component';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {SearchResultData} from '../../data/search-result-data';
import {SearchCharactersModalComponent} from '../search-characters-modal/search-characters-modal.component';
import {CharacterRankingData} from '../../data/lists/character-ranking-data';
import {CharacterRankingsData} from '../../data/character-rankings-data';
import {UpdateCharacterRankingDto} from '../../data/dto/update-list-ranks-dto';
import {ProfileService} from '../../services/profile.service';
import {ConfirmationService} from '../../services/confirmation.service';

@Component({
  selector: 'app-ranked-character-list-full',
  imports: [RouterLink, CdkDropList, CdkDrag],
  templateUrl: './ranked-character-list-full.component.html',
  styleUrl: './ranked-character-list-full.component.css',
  standalone: true,
})
export class RankedCharacterListFullComponent {
  @Input({transform: booleanAttribute}) editable: boolean = false;
  @Input({required: true}) characterRankings: CharacterRankingsData;

  readonly typeTitles: string[] = [
    'Protagonists',
    'Deuteragonists',
    'Antagonists',
    'Tritagonists',
    'Side Characters',
  ];

  readonly validCharacterTypes: string[] = [
    'protagonists',
    'deuteragonists',
    'antagonists',
    'tritagonists',
    'side',
  ];

  selectedShow: SearchResultData | null = null;
  characterType: string;

  constructor(private modalService: NgbModal,
              private profileService: ProfileService,
              private confirmationService: ConfirmationService,
              private route: ActivatedRoute,
              private router: Router) {
    this.route.params.subscribe((params) => {
      this.characterType = params['type'];
    });

    // Check to ensure the type in the route is valid, if not redirect to 404
    if (!this.validCharacterTypes.includes(this.characterType)) {
      this.router.navigate(['not-found']);
    }
  }

  async openSearchShowsModal() {
    const searchShowsModalRef = this.modalService.open(SearchShowsModalComponent, {
        ariaLabelledBy: 'searchShowsModal',
        centered: true,
    });
    searchShowsModalRef.componentInstance.modalTitle = 'Add Character to Ranking List';

    this.selectedShow = await searchShowsModalRef.result;
    await this.openSearchCharactersModal();
  }

  async openSearchCharactersModal() {
    try {
      const searchCharactersModalRef = this.modalService.open(SearchCharactersModalComponent, {
        ariaLabelledBy: 'searchCharactersModal',
        centered: true
      });

      searchCharactersModalRef.componentInstance.selectedShow = this.selectedShow;
      searchCharactersModalRef.componentInstance.selectedCharacterType = this.characterType;
      searchCharactersModalRef.componentInstance.onAdd = (character: CharacterRankingData) => {
        character.rankNum = this.selectedCharacterRankings.length + 1;
        this.selectedCharacterRankings.push(character);
      };

      await searchCharactersModalRef.result;
    } catch (modalDismissReason) {
      if (modalDismissReason === 'backFromCharacters') {
        await this.openSearchShowsModal();
      }
    }
  }

  /**
   * Returns all the rankings for the selected character type
   */
  get selectedCharacterRankings(): CharacterRankingData[] {
    return this.characterRankings[this.characterType];
  }

  characterTypeTitle(type?: string): string {
    if (type) {
      return this.typeTitles[this.validCharacterTypes.indexOf(type)];
    } else {
      return this.typeTitles[this.validCharacterTypes.indexOf(this.characterType)];
    }
  }

  drop(event: CdkDragDrop<string[]>) {
    moveItemInArray(this.characterRankings[this.characterType], event.previousIndex, event.currentIndex,);

    // Update the rank numbers based on the index within the updated list
    this.characterRankings[this.characterType].forEach((character, index) => {
      character.rankNum = index + 1;
    });
    this.updateCharacterRankingList();
  }

  async updateCharacterRankingList() {
    try {
      const data: UpdateCharacterRankingDto = {
        characterType: this.characterType != 'side' ? this.characterType.slice(0, -1) : this.characterType,
        updates: this.characterRankings[this.characterType].map((character) => ({
            id: character.id,
            rankNum: character.rankNum
        })
      )};

      await this.profileService.updateCharacterRankingList(data);
    } catch (error) {
      console.error(error);
    }
  }

  async removeCharacter(removeCharacter: CharacterRankingData) {
    try {
      const confirmation = await this.confirmationService.confirmRemove(removeCharacter.name);

      if (confirmation) {
        const response = await this.profileService.removeCharacterFromRankingList(removeCharacter.id);
        if (response.ok) {
          this.characterRankings[this.characterType] = this.characterRankings[this.characterType].filter((character) => character.id !== removeCharacter.id);
        }
      }
    } catch (error) {
      console.error(error);
    }
  }
}
