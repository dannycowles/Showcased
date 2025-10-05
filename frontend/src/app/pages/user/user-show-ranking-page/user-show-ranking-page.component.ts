import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, RouterLink} from '@angular/router';
import {ShowRankingData} from '../../../data/lists/show-ranking-data';
import {UserService} from '../../../services/user.service';
import {Title} from '@angular/platform-browser';
import {NgOptimizedImage} from '@angular/common';
import {ShowListFullComponent} from '../../../components/show-list-full/show-list-full.component';

@Component({
  selector: 'app-user-show-ranking-page',
  templateUrl: './user-show-ranking-page.component.html',
  styleUrl: './user-show-ranking-page.component.css',
  imports: [RouterLink, NgOptimizedImage, ShowListFullComponent],
  standalone: true,
})
export class UserShowRankingPageComponent implements OnInit {
  readonly username: string;
  rankingEntries: ShowRankingData[] = [];
  loadingData: boolean = true;

  constructor(
    private route: ActivatedRoute,
    private userService: UserService,
    private title: Title,
  ) {
    this.username = this.route.snapshot.params['username'];
    this.title.setTitle(`${this.username}'s Show Ranking | Showcased`);
  }

  async ngOnInit() {
    // Retrieve full show ranking list for user
    try {
      this.rankingEntries = await this.userService.getFullShowRankingList(this.username);
    } catch (error) {
      console.error(error);
    } finally {
      this.loadingData = false;
    }
  }
}
