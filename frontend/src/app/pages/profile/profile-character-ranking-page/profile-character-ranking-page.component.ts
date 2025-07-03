import {Component, OnInit} from '@angular/core'
import {ActivatedRoute, Router} from '@angular/router';
import {ProfileService} from '../../../services/profile.service';
import {CharacterRankingsData} from '../../../data/character-rankings-data';
import {CharacterRankingData} from '../../../data/lists/character-ranking-data';
import {CdkDragDrop, moveItemInArray} from '@angular/cdk/drag-drop';
import {UtilsService} from '../../../services/utils.service';
import {ShowService} from '../../../services/show.service';
import {ResultPageData} from '../../../data/show/result-page-data';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {SearchShowsModalComponent} from '../../../components/search-shows-modal/search-shows-modal.component';
import {SearchCharactersModalComponent} from '../../../components/search-characters-modal/search-characters-modal.component';

@Component({
  selector: 'app-profile-character-ranking-page',
  templateUrl: './profile-character-ranking-page.component.html',
  styleUrl: './profile-character-ranking-page.component.css',
  standalone: false
})
export class ProfileCharacterRankingPageComponent implements OnInit {
  characterRankings: CharacterRankingsData;
  readonly validCharacterTypes: string[] = ["protagonists", "deuteragonists", "antagonists", "tritagonists", "side"];
  readonly typeTitles: string[] = ["Protagonists", "Deuteragonists", "Antagonists", "Tritagonists", "Side Characters"];
  characterType: string;

  debouncedSearchShows: () => void;
  searchShowString: string = "";
  searchShowResults: ResultPageData;

  selectedShowId: number | null = null;
  selectedShowTitle: string = "";

  constructor(private route: ActivatedRoute,
              private profileService: ProfileService,
              private router: Router,
              public utils: UtilsService,
              private showService: ShowService,
              private modalService: NgbModal) {
    this.route.params.subscribe(params => {
      this.characterType = params['type'];
    });

    // Check to ensure the type in the route is valid, if not redirect to 404
    if (!this.validCharacterTypes.includes(this.characterType)) {
      this.router.navigate(['not-found']);
    }

    this.debouncedSearchShows = this.utils.debounce(() => this.searchShows());
  };

  async ngOnInit() {
    // Retrieve all character rankings from the backend
    try {
      this.characterRankings = await this.profileService.getCharacterRankingLists();
    } catch (error) {
      console.error(error);
    }
  }

  async openSearchShowsModal() {
    const searchShowsModalRef = this.modalService.open(SearchShowsModalComponent, {
      ariaLabelledBy: "searchShowsModal",
      centered: true
    });
    searchShowsModalRef.componentInstance.modalTitle = "Add Character to Ranking List";

    const searchResult = await searchShowsModalRef.result;
    this.selectedShowId = searchResult.selectedShowId;
    this.selectedShowTitle = searchResult.selectedShowTitle;

    await this.openSearchCharactersModal();
  }

  async openSearchCharactersModal() {
    try {
      const searchCharactersModalRef = this.modalService.open(SearchCharactersModalComponent, {
        ariaLabelledBy: "searchCharactersModal",
        centered: true
      });

      searchCharactersModalRef.componentInstance.selectedShow = null;
      searchCharactersModalRef.componentInstance.selectedCharacterType =  this.characterType;
      searchCharactersModalRef.componentInstance.onAdd = (character: CharacterRankingData) => {
        // add rank num based on proper list
        // add to proper list
      }

      const response = await searchCharactersModalRef.result;
    } catch (modalDismissReason) {
      if (modalDismissReason === "backFrom") {
        await this.openSearchShowsModal();
      }
    }
  }

  characterTypeTitle(type?: string): string {
    if (type) {
      return this.typeTitles[this.validCharacterTypes.indexOf(type)];
    } else {
      return this.typeTitles[this.validCharacterTypes.indexOf(this.characterType)];
    }
  }

  async searchShows() {
    try {
      this.searchShowResults = await this.showService.searchForShows(this.searchShowString);
    } catch (error) {
      console.error(error);
    }
  }

  /**
   * Returns all the rankings for the selected character type
   */
  get selectedCharacterRankings(): CharacterRankingData[] {
    return this.characterRankings[this.characterType];
  }

  drop(event: CdkDragDrop<string[]>) {
    moveItemInArray(this.characterRankings[this.characterType], event.previousIndex, event.currentIndex);

    // Update the rank numbers based on the index within the updated list
    this.characterRankings[this.characterType].forEach((character, index) => {
      character.rankNum = index + 1;
    });
    this.updateCharacterRankingList();
  }

  async updateCharacterRankingList() {
    try {
      const data = {
        characterType: this.characterType != "side" ? this.characterType.slice(0,this.characterType.length-1) : this.characterType,
        updates: this.characterRankings[this.characterType].map(character => ({
          id: character.id,
          rankNum: character.rankNum
        }))
      }

      await this.profileService.updateCharacterRankingList(data);
    } catch (error) {
      console.error(error);
    }
  }

  async removeCharacter(removeId: string) {
    try {
      const response = await this.profileService.removeCharacterFromRankingList(removeId);
      if (response.ok) {
        this.characterRankings[this.characterType] = this.characterRankings[this.characterType].filter(character => character.id !== removeId);
      }
    } catch (error) {
      console.error(error);
    }
  }
}
