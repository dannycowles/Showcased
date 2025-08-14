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
  readonly username: string;
  rankingEntries: SeasonRankingData[];

  constructor(private userService: UserService,
              private route: ActivatedRoute) {
    this.username = this.route.snapshot.params['username'];
  };

  async ngOnInit() {
    try {
      this.rankingEntries = await this.userService.getFullSeasonRankingList(this.username);
    } catch(error) {
      console.error(error);
    }
  };
}
