import {Component, OnInit} from '@angular/core'
import {ActivatedRoute, Router} from '@angular/router';
import {ProfileService} from '../../../services/profile.service';
import {CharacterRankingsData} from '../../../data/character-rankings-data';
import {CharacterRankingData} from '../../../data/lists/character-ranking-data';

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

  async ngOnInit() {
    // Retrieve all character rankings from the backend
    try {
      this.characterRankings = await this.profileService.getCharacterRankingLists();
    } catch (error) {
      console.error(error);
    }
  }

}
