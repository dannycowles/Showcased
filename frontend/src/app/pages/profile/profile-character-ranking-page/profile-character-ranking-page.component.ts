import {Component, OnInit} from '@angular/core'
import {ActivatedRoute, Router, RouterLink} from '@angular/router';
import {CharacterRankingsData} from '../../../data/character-rankings-data';
import {ProfileService} from '../../../services/profile.service';
import {NgOptimizedImage} from '@angular/common';
import {CharacterListFullComponent} from '../../../components/character-list-full/character-list-full.component';
import {CharacterRankingData} from '../../../data/lists/character-ranking-data';

@Component({
  selector: 'app-profile-character-ranking-page',
  templateUrl: './profile-character-ranking-page.component.html',
  styleUrl: './profile-character-ranking-page.component.css',
  imports: [
    RouterLink,
    NgOptimizedImage,
    CharacterListFullComponent,
  ],
  standalone: true,
})
export class ProfileCharacterRankingPageComponent implements OnInit {
  characterRankings: CharacterRankingsData;
  loadingData: boolean = true;

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

  selectedCharacterType: string;
  selectedCharacters: CharacterRankingData[] = [];

  constructor(private profileService: ProfileService,
              private route: ActivatedRoute,
              private router: Router) {
  }

  async ngOnInit() {
    // Retrieve all character rankings from the backend
    try {
      this.characterRankings = await this.profileService.getCharacterRankingLists();

      this.route.params.subscribe((params) => {
        this.selectedCharacterType = params['type'];

        // Ensure type is valid, if not route them to the 404 page
        if (!this.validCharacterTypes.includes(this.selectedCharacterType)) {
          this.router.navigate(['not-found']);
        }

        this.selectedCharacters = this.characterRankings[this.selectedCharacterType];
      });
    } catch (error) {
      console.error(error);
    } finally {
      this.loadingData = false;
    }
  }

  characterTypeTitle(type?: string): string {
    if (type) {
      return this.typeTitles[this.validCharacterTypes.indexOf(type)];
    } else {
      return this.typeTitles[this.validCharacterTypes.indexOf(this.selectedCharacterType)];
    }
  }
}
