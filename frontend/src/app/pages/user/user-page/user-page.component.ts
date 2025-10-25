import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, RouterLink} from '@angular/router';
import {UserData} from '../../../data/user-data';
import {UserService} from '../../../services/user.service';
import {UtilsService} from '../../../services/utils.service';
import {ReviewType} from '../../../data/enums';
import {Title} from '@angular/platform-browser';
import {UserInfoComponent} from '../../../components/user-info/user-info.component';
import {CollectionComponent} from '../../../components/collection/collection.component';
import {RecentReviewsComponent} from '../../../components/recent-reviews/recent-reviews.component';
import {CharacterRankingData} from '../../../data/lists/character-ranking-data';
import {NgOptimizedImage} from '@angular/common';

@Component({
  selector: 'app-user-page',
  templateUrl: './user-page.component.html',
  styleUrl: './user-page.component.css',
  imports: [
    UserInfoComponent,
    RouterLink,
    CollectionComponent,
    RecentReviewsComponent,
    NgOptimizedImage,
  ],
  standalone: true,
})
export class UserPageComponent implements OnInit {
  readonly username: string;
  userDetails: UserData;
  readonly ReviewType = ReviewType;

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
    private route: ActivatedRoute,
    private userService: UserService,
    public utilsService: UtilsService,
    private title: Title,
  ) {
    this.username = this.route.snapshot.params['username'];
    this.title.setTitle(`${this.username} | Showcased`);
  }

  async ngOnInit() {
    // Retrieve user details from the backend
    try {
      this.userDetails = await this.userService.getUserDetails(this.username);
      this.setSelectedCharacterType('protagonists');
    } catch (error) {
      console.error(error);
    }
  }

  setSelectedCharacterType(type: string) {
    this.selectedCharacterType = type;
    this.selectedCharacters = this.userDetails.characterRankings[this.selectedCharacterType];
  }

  characterTypeTitle(type?: string): string {
    if (type) {
      return this.typeTitles[this.validCharacterTypes.indexOf(type)];
    } else {
      return this.typeTitles[this.validCharacterTypes.indexOf(this.selectedCharacterType)];
    }
  }
}
