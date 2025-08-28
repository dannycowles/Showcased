import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, RouterLink} from '@angular/router';
import {UserData} from '../../../data/user-data';
import {UserService} from '../../../services/user.service';
import {UtilsService} from '../../../services/utils.service';
import {ReviewType} from '../../../data/enums';
import {Title} from '@angular/platform-browser';
import {UserInfoComponent} from '../../../components/user-info/user-info.component';
import {ShowListComponent} from '../../../components/show-list/show-list.component';
import {SeasonListComponent} from '../../../components/season-list/season-list.component';
import {EpisodeListComponent} from '../../../components/episode-list/episode-list.component';
import {CharacterListComponent} from '../../../components/character-list/character-list.component';
import {DynamicListComponent} from '../../../components/dynamic-list/dynamic-list.component';
import {ProfileReviewComponent} from '../../../components/profile-review/profile-review.component';
import {CollectionComponent} from '../../../components/collection/collection.component';
import {ReviewChartComponent} from '../../../components/review-chart/review-chart.component';

@Component({
  selector: 'app-user-page',
  templateUrl: './user-page.component.html',
  styleUrl: './user-page.component.css',
  imports: [
    UserInfoComponent,
    ShowListComponent,
    RouterLink,
    SeasonListComponent,
    EpisodeListComponent,
    CharacterListComponent,
    DynamicListComponent,
    ProfileReviewComponent,
    CollectionComponent,
    ReviewChartComponent,
  ],
  standalone: true,
})
export class UserPageComponent implements OnInit {
  readonly username: string;
  userDetails: UserData;
  readonly ReviewType = ReviewType;

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
    } catch (error) {
      console.error(error);
    }
  }
}
