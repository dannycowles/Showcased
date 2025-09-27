import {Component, OnInit} from '@angular/core';
import {UserData} from '../../../data/user-data';
import {ProfileService} from '../../../services/profile.service';
import {UtilsService} from '../../../services/utils.service';
import {ReviewType} from '../../../data/enums';
import {UserInfoComponent} from '../../../components/user-info/user-info.component';
import {ShowListComponent} from '../../../components/show-list/show-list.component';
import {SeasonListComponent} from '../../../components/season-list/season-list.component';
import {EpisodeListComponent} from '../../../components/episode-list/episode-list.component';
import {CharacterListComponent} from '../../../components/character-list/character-list.component';
import {DynamicListComponent} from '../../../components/dynamic-list/dynamic-list.component';
import {ProfileReviewComponent} from '../../../components/profile-review/profile-review.component';
import {RouterLink} from '@angular/router';
import {CollectionComponent} from '../../../components/collection/collection.component';
import {ReviewChartComponent} from '../../../components/review-chart/review-chart.component';
import {CharacterRankingData} from '../../../data/lists/character-ranking-data';
import {RecentReviewsComponent} from '../../../components/recent-reviews/recent-reviews.component';

@Component({
  selector: 'app-profile-page',
  templateUrl: './profile-page.component.html',
  styleUrl: './profile-page.component.css',
  imports: [
    UserInfoComponent,
    ShowListComponent,
    SeasonListComponent,
    EpisodeListComponent,
    CharacterListComponent,
    DynamicListComponent,
    RouterLink,
    CollectionComponent,
    RecentReviewsComponent,
  ],
  standalone: true,
})
export class ProfilePageComponent implements OnInit {
  profileData: UserData;

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

  selectedCharacterType: string = 'protagonists';
  selectedCharacters: CharacterRankingData[];

  constructor(
    private profileService: ProfileService,
    public utilsService: UtilsService,
  ) {}

  async ngOnInit() {
    // Retrieve profile data from backend
    try {
      this.profileData = await this.profileService.getProfileDetails();
      this.setSelectedCharacterType('protagonists');
    } catch (error) {
      console.error(error);
    }
  }

  setSelectedCharacterType(type: string) {
    this.selectedCharacterType = type;
    this.selectedCharacters = this.profileData.characterRankings[this.selectedCharacterType];
  }

  characterTypeTitle(type?: string): string {
    if (type) {
      return this.typeTitles[this.validCharacterTypes.indexOf(type)];
    } else {
      return this.typeTitles[this.validCharacterTypes.indexOf(this.selectedCharacterType)];
    }
  }

  protected readonly ReviewType = ReviewType;
}
