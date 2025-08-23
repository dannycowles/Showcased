import {Component, OnInit} from '@angular/core';
import {DynamicRankingData} from '../../../data/lists/dynamic-ranking-data';
import {UserService} from '../../../services/user.service';
import {ActivatedRoute, RouterLink} from '@angular/router';
import {Title} from '@angular/platform-browser';
import {
  RankedDynamicListFullComponent
} from '../../../components/ranked-dynamic-list-full/ranked-dynamic-list-full.component';

@Component({
  selector: 'app-user-dynamic-ranking-page',
  templateUrl: './user-dynamic-ranking-page.component.html',
  styleUrl: './user-dynamic-ranking-page.component.css',
  imports: [RouterLink, RankedDynamicListFullComponent],
  standalone: true,
})
export class UserDynamicRankingPageComponent implements OnInit {
  readonly username: string;
  dynamics: DynamicRankingData[];

  constructor(
    private userService: UserService,
    private route: ActivatedRoute,
    private title: Title,
  ) {
    this.username = this.route.snapshot.params['username'];
    this.title.setTitle(`${this.username}'s Dynamic Ranking | Showcased`);
  }

  async ngOnInit() {
    try {
      this.dynamics = await this.userService.getFullDynamicRankingList(
        this.username,
      );
    } catch (error) {
      console.error(error);
    }
  }
}
