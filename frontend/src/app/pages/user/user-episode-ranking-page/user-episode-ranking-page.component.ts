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
  userId: number;
  rankingEntries: EpisodeRankingData[];

  constructor(private route: ActivatedRoute,
              private userService: UserService) {};

  async ngOnInit() {
    this.userId = this.route.snapshot.params['id'];

    // Retrieve full episode ranking list for user
    try {
      this.rankingEntries = await this.userService.getFullEpisodeRankingList(this.userId);
    } catch(error) {
      console.error(error);
    }
  };

  returnToUserProfile() {
    window.location.href = `/user/${this.userId}`;
  }

}
