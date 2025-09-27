import {Component, OnInit} from '@angular/core';
import {ProfileService} from '../../../services/profile.service';
import {CharacterRankingsData} from '../../../data/character-rankings-data';
import {NgOptimizedImage} from '@angular/common';
import {ActivatedRoute, Router, RouterLink} from '@angular/router';
import {
  RankedCharacterListFullComponent
} from '../../../components/ranked-character-list-full/ranked-character-list-full.component';
import {SearchShowsModalComponent} from '../../../components/search-shows-modal/search-shows-modal.component';
import {
  SearchCharactersModalComponent
} from '../../../components/search-characters-modal/search-characters-modal.component';
import {CharacterRankingData} from '../../../data/lists/character-ranking-data';
import {SearchResultData} from '../../../data/search-result-data';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {UpdateCharacterRankingDto} from '../../../data/dto/update-list-ranks-dto';

@Component({
  selector: 'app-profile-edit-character-ranking-page',
  imports: [
    NgOptimizedImage,
    RouterLink,
    RankedCharacterListFullComponent,
  ],
  templateUrl: './profile-edit-character-ranking-page.component.html',
  styleUrl: './profile-edit-character-ranking-page.component.css',
  standalone: true,
})
export class ProfileEditCharacterRankingPageComponent implements OnInit {
  rankingEntries: CharacterRankingsData;

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
  selectedCharacterType: string;
  selectedCharacters: CharacterRankingData[];

  constructor(private profileService: ProfileService,
              private modalService: NgbModal,
              private route: ActivatedRoute,
              private router: Router) {}

  async ngOnInit() {
    try {
      this.rankingEntries = await this.profileService.getCharacterRankingLists();

      this.route.params.subscribe((params) => {
        this.selectedCharacterType = params['type'];

        // Ensure type is valid, if not route them to the 404 page
        if (!this.validCharacterTypes.includes(this.selectedCharacterType)) {
          this.router.navigate(['not-found']);
        }

        this.selectedCharacters = this.rankingEntries[this.selectedCharacterType];
      });
    } catch (error) {
      console.error(error);
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
      searchCharactersModalRef.componentInstance.selectedCharacterType = this.selectedCharacterType;
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
    return this.rankingEntries[this.selectedCharacterType];
  }

  characterTypeTitle(type?: string): string {
    if (type) {
      return this.typeTitles[this.validCharacterTypes.indexOf(type)];
    } else {
      return this.typeTitles[this.validCharacterTypes.indexOf(this.selectedCharacterType)];
    }
  }

  async updateCharacterRankingList() {
    try {
      const data: UpdateCharacterRankingDto = {
        characterType: this.selectedCharacterType != 'side' ? this.selectedCharacterType.slice(0, -1) : this.selectedCharacterType,
        updates: this.selectedCharacterRankings.map((character) => ({
            id: character.id,
            rankNum: character.rankNum
          })
        )};

      await this.profileService.updateCharacterRankingList(data);
    } catch (error) {
      console.error(error);
    }
  }

  async removeFromCharacterRankingList(removeId: string) {
    try {
      const response = await this.profileService.removeCharacterFromRankingList(removeId);
      if (response.ok) {
        this.selectedCharacters = this.selectedCharacters.filter((character) => character.id !== removeId);
      }
    } catch (error) {
      console.error(error);
    }
  }
}
