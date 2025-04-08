import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {ShowRankingData} from '../../../data/lists/show-ranking-data';
import {UserService} from '../../../services/user.service';

@Component({
  selector: 'app-user-show-ranking-page',
  templateUrl: './user-show-ranking-page.component.html',
  styleUrl: './user-show-ranking-page.component.css',
  standalone: false
})
export class UserShowRankingPageComponent implements OnInit {
  userId: number;
  rankingEntries: ShowRankingData[];

  constructor(private route: ActivatedRoute,
              private userService: UserService) { };

  async ngOnInit() {
    this.userId = this.route.snapshot.params['id'];

    // Retrieve full show ranking list for user
    try {
      this.rankingEntries = await this.userService.getFullShowRankingList(this.userId);
    } catch (error) {
      console.error(error);
    }
  };

  returnToUserProfile() {
    window.location.href = `/user/${this.userId}`;
  }

}
