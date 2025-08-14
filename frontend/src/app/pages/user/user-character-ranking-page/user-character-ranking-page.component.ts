import {Component, OnInit} from '@angular/core';
import {UserService} from '../../../services/user.service';
import {ActivatedRoute, Router} from '@angular/router';
import {CharacterRankingsData} from '../../../data/character-rankings-data';
import {CharacterRankingData} from '../../../data/lists/character-ranking-data';

@Component({
  selector: 'app-user-character-ranking-page',
  templateUrl: './user-character-ranking-page.component.html',
  styleUrl: './user-character-ranking-page.component.css',
  standalone: false
})
export class UserCharacterRankingPageComponent implements OnInit {
  characterRankings: CharacterRankingsData;
  readonly validCharacterTypes: string[] = ["protagonists", "deuteragonists", "antagonists", "tritagonists", "side"];
  characterType: string;
  readonly username: string

  constructor(private userService: UserService,
              private route: ActivatedRoute,
              private router: Router) {
    this.username = this.route.snapshot.params["username"];
    this.route.params.subscribe(params => {
      this.characterType = params["type"];
    });

    // Check to ensure the type in the route is valid, if not redirect to 404
    if (!this.validCharacterTypes.includes(this.characterType)) {
      this.router.navigate(['not-found']);
    }
  };

  async ngOnInit() {
    // Retrieve all character rankings from backend
    try {
      this.characterRankings = await this.userService.getAllCharacterRankingLists(this.username);
    } catch(error) {
      console.error(error);
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
    return this.characterRankings[this.characterType];
  }

}
