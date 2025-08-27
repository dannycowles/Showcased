import {Component, OnInit} from '@angular/core'
import { RouterLink} from '@angular/router';

import {RankedCharacterListFullComponent} from '../../../components/ranked-character-list-full/ranked-character-list-full.component';
import {CharacterRankingsData} from '../../../data/character-rankings-data';
import {ProfileService} from '../../../services/profile.service';

@Component({
  selector: 'app-profile-character-ranking-page',
  templateUrl: './profile-character-ranking-page.component.html',
  styleUrl: './profile-character-ranking-page.component.css',
  imports: [RouterLink, RankedCharacterListFullComponent],
  standalone: true,
})
export class ProfileCharacterRankingPageComponent implements OnInit {
  characterRankings: CharacterRankingsData;

  constructor(private profileService: ProfileService) {};

  async ngOnInit() {
    // Retrieve all character rankings from the backend
    try {
      this.characterRankings = await this.profileService.getCharacterRankingLists();
    } catch (error) {
      console.error(error);
    }
  }
}
