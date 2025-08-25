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
    ProfileReviewComponent,
    RouterLink,
    CollectionComponent,
  ],
  standalone: true,
})
export class ProfilePageComponent implements OnInit {
  profileData: UserData;

  constructor(
    private profileService: ProfileService,
    public utilsService: UtilsService,
  ) {}

  async ngOnInit() {
    // Retrieve profile data from backend
    try {
      this.profileData = await this.profileService.getProfileDetails();
    } catch (error) {
      console.error(error);
    }
  }

  protected readonly ReviewType = ReviewType;
}
