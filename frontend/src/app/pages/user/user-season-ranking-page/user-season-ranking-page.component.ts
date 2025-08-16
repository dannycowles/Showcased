import {Component, OnInit} from '@angular/core';
import {UserService} from '../../../services/user.service';
import {ActivatedRoute} from '@angular/router';
import {SeasonRankingData} from '../../../data/lists/season-ranking-data';
import {Title} from '@angular/platform-browser';

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
              private route: ActivatedRoute,
              private title: Title) {
    this.username = this.route.snapshot.params['username'];
    this.title.setTitle(`${this.username}'s Season Ranking | Showcased`);
  };

  async ngOnInit() {
    try {
      this.rankingEntries = await this.userService.getFullSeasonRankingList(this.username);
    } catch(error) {
      console.error(error);
    }
  };
}
