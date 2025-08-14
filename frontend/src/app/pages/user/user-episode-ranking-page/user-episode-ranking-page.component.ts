import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {EpisodeRankingData} from '../../../data/lists/episode-ranking-data';
import {UserService} from '../../../services/user.service';

@Component({
  selector: 'app-user-episode-ranking-page',
  templateUrl: './user-episode-ranking-page.component.html',
  styleUrl: './user-episode-ranking-page.component.css',
  standalone: false
})
export class UserEpisodeRankingPageComponent implements OnInit {
  readonly username: string;
  rankingEntries: EpisodeRankingData[];

  constructor(private route: ActivatedRoute,
              private userService: UserService) {
    this.username = this.route.snapshot.params['username'];
  };

  async ngOnInit() {
    // Retrieve full episode ranking list for user
    try {
      this.rankingEntries = await this.userService.getFullEpisodeRankingList(this.username);
    } catch(error) {
      console.error(error);
    }
  };
}
