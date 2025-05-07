import {Component, OnInit} from '@angular/core'
import {ActivatedRoute, Router} from '@angular/router';
import {ProfileService} from '../../../services/profile.service';
import {CharacterRankingsData} from '../../../data/character-rankings-data';
import {CharacterRankingData} from '../../../data/lists/character-ranking-data';
import {CdkDragDrop, moveItemInArray} from '@angular/cdk/drag-drop';

@Component({
  selector: 'app-profile-character-ranking-page',
  templateUrl: './profile-character-ranking-page.component.html',
  styleUrl: './profile-character-ranking-page.component.css',
  standalone: false
})
export class ProfileCharacterRankingPageComponent implements OnInit {
  characterRankings: CharacterRankingsData;
  readonly validCharacterTypes: string[] = ["protagonists", "deuteragonists", "antagonists"];
  characterType: string;

  constructor(private route: ActivatedRoute,
              private profileService: ProfileService,
              private router: Router) {
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
  }

  uppercaseCharacterType(type?: string): string {
    if (type) {
      return type.charAt(0).toUpperCase() + type.slice(1);
    } else {
      return this.characterType.charAt(0).toUpperCase() + this.characterType.slice(1);
    }
  }

  /**
   * Returns all the rankings for the selected character type
   */
  get selectedCharacterRankings(): CharacterRankingData[] {
    if (this.characterType === this.validCharacterTypes[0]) {
      return this.characterRankings.protagonists;
    } else if (this.characterType === this.validCharacterTypes[1]) {
      return this.characterRankings.deuteragonists;
    } else {
      return this.characterRankings.antagonists;
    }
  }

  drop(event: CdkDragDrop<string[]>) {
    moveItemInArray(this.characterRankings[this.characterType], event.previousIndex, event.currentIndex);

    // Update the rank numbers based on the index within the updated list
    this.characterRankings[this.characterType].forEach((character, index) => {
      character.rankNum = index + 1;
    });
    this.updateCharacterRankingList();
  }

  async removeCharacterFromRankingList(name: string) {
    try {
      await this.profileService.removeCharacterFromRankingList(this.characterType, name);

      // Remove the character from entries shown to the user
      this.characterRankings[this.characterType].filter(character => character.name != name);
    } catch(error) {
      console.error(error);
    }
  }

  async updateCharacterRankingList() {
    try {
      const updates = {
        characterType: this.characterType.slice(0, this.characterType.length - 1),
        updates: this.characterRankings[this.characterType].map(character => ({
          characterName: character.name,
          showName: character.show,
          rankNum: character.rankNum
        }))
      };
      await this.profileService.updateCharacterRankingList(updates);
    } catch(error) {
      console.error(error);
    }

  }

}
