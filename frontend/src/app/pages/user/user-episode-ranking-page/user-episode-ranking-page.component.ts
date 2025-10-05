import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, RouterLink} from '@angular/router';
import {EpisodeRankingData} from '../../../data/lists/episode-ranking-data';
import {UserService} from '../../../services/user.service';
import {Title} from '@angular/platform-browser';
import {EpisodeListFullComponent} from '../../../components/episode-list-full/episode-list-full.component';
import {NgOptimizedImage} from '@angular/common';

@Component({
  selector: 'app-user-episode-ranking-page',
  templateUrl: './user-episode-ranking-page.component.html',
  styleUrl: './user-episode-ranking-page.component.css',
  imports: [RouterLink, EpisodeListFullComponent, NgOptimizedImage],
  standalone: true,
})
export class UserEpisodeRankingPageComponent implements OnInit {
  readonly username: string;
  rankingEntries: EpisodeRankingData[] = [];
  loadingData: boolean = true;

  constructor(
    private route: ActivatedRoute,
    private userService: UserService,
    private title: Title,
  ) {
    this.username = this.route.snapshot.params['username'];
    this.title.setTitle(`${this.username}'s Episode Ranking | Showcased`);
  }

  async ngOnInit() {
    // Retrieve full episode ranking list for user
    try {
      this.rankingEntries = await this.userService.getFullEpisodeRankingList(this.username);
    } catch (error) {
      console.error(error);
    } finally {
      this.loadingData = false;
    }
  }
}
