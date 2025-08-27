import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, RouterLink} from '@angular/router';
import {RankedCharacterListFullComponent} from '../../../components/ranked-character-list-full/ranked-character-list-full.component';
import {CharacterRankingsData} from '../../../data/character-rankings-data';
import {UserService} from '../../../services/user.service';

@Component({
  selector: 'app-user-character-ranking-page',
  templateUrl: './user-character-ranking-page.component.html',
  styleUrl: './user-character-ranking-page.component.css',
  imports: [RouterLink, RankedCharacterListFullComponent],
  standalone: true,
})
export class UserCharacterRankingPageComponent implements OnInit {
  readonly username: string;
  characterRankings: CharacterRankingsData;

  constructor(private userService: UserService,
              private route: ActivatedRoute) {
    this.username = this.route.snapshot.params['username'];
  };

  async ngOnInit() {
    // Retrieve all character rankings from the backend
    try {
      this.characterRankings = await this.userService.getAllCharacterRankingLists(this.username);
    } catch (error) {
      console.error(error);
    }
  }
}
