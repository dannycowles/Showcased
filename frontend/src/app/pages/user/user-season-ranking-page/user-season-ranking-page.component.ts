import {Component, OnInit} from '@angular/core';
import {UserService} from '../../../services/user.service';
import {ActivatedRoute} from '@angular/router';
import {SeasonRankingData} from '../../../data/lists/season-ranking-data';

@Component({
  selector: 'app-user-season-ranking-page',
  templateUrl: './user-season-ranking-page.component.html',
  styleUrl: './user-season-ranking-page.component.css',
  standalone: false
})
export class UserSeasonRankingPageComponent implements OnInit {
  readonly userId: number;
  rankingEntries: SeasonRankingData[];

  constructor(private userService: UserService,
              private route: ActivatedRoute) {
    this.userId = this.route.snapshot.params['id'];
  };

  async ngOnInit() {
    try {
      this.rankingEntries = await this.userService.getFullSeasonRankingList(this.userId);
    } catch(error) {
      console.error(error);
    }
  };

}
