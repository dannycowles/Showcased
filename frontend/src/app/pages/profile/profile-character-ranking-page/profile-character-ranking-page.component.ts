import {Component, OnInit} from '@angular/core'
import {ActivatedRoute, Router} from '@angular/router';
import {ProfileService} from '../../../services/profile.service';
import {CharacterRankingsData} from '../../../data/character-rankings-data';
import {CharacterRankingData} from '../../../data/lists/character-ranking-data';
import {CdkDragDrop, moveItemInArray} from '@angular/cdk/drag-drop';
import {UtilsService} from '../../../services/utils.service';
import {ShowService} from '../../../services/show.service';
import {TopRatedShowsData} from '../../../data/top-rated-shows-data';
import {SearchResultData} from '../../../data/search-result-data';
import {RoleData} from '../../../data/role-data';

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
  searchShowResults: TopRatedShowsData;
  selectedShow: SearchResultData;

  debouncedSearchCharacters: () => void;
  searchCharacterString: string = "";
  searchCharacterResults: RoleData[];
  selectedCharacter: RoleData;

  constructor(private route: ActivatedRoute,
              private profileService: ProfileService,
              private router: Router,
              public utils: UtilsService,
              private showService: ShowService) {
    this.route.params.subscribe(params => {
      this.characterType = params['type'];
    });

    // Check to ensure the type in the route is valid, if not redirect to 404
    if (!this.validCharacterTypes.includes(this.characterType)) {
      this.router.navigate(['not-found']);
    }
  };

  async ngOnInit() {
    // Retrieve all character rankings from the backend
    try {
      this.characterRankings = await this.profileService.getCharacterRankingLists();
    } catch (error) {
      console.error(error);
    }

    this.debouncedSearchShows = this.utils.debounce(() => {
      this.searchShows();
    });
    this.debouncedSearchCharacters = this.utils.debounce(() => {
      this.searchCharacters();
    });
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

  async searchCharacters() {
    try {
      this.searchCharacterResults = await this.showService.searchCharacters(this.selectedShow.id, this.searchCharacterString);
    } catch (error) {
      console.error(error);
    }
  }

  backButtonPressed() {
    this.searchCharacterResults = null;
    this.searchCharacterString = "";
    this.selectedCharacter = null;
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

  async addCharacter() {
    try {
      const data = {
        id: this.selectedCharacter.id,
        showId: this.selectedShow.id,
        name: this.selectedCharacter.characterName,
        type: this.characterType != "side" ? this.characterType.slice(0,this.characterType.length-1) : this.characterType
      }

      const response = await this.profileService.addCharacterToRankingList(data);
      if (response.ok) {
        const newData = {
          id: this.selectedCharacter.id,
          showId: this.selectedShow.id,
          characterName: this.selectedCharacter.characterName,
          showName: this.selectedShow.name
        }
        this.characterRankings[this.characterType].push(new CharacterRankingData(newData));
      }
    } catch (error) {
      console.error(error);
    }
  }
}
