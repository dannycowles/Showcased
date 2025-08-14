import {Component, OnInit} from '@angular/core';
import {DynamicRankingData} from '../../../data/lists/dynamic-ranking-data';
import {UserService} from '../../../services/user.service';
import {ActivatedRoute} from '@angular/router';

@Component({
  selector: 'app-user-dynamic-ranking-page',
  templateUrl: './user-dynamic-ranking-page.component.html',
  styleUrl: './user-dynamic-ranking-page.component.css',
  standalone: false
})
export class UserDynamicRankingPageComponent implements OnInit {
  readonly username: string;
  dynamics: DynamicRankingData[];

  constructor(private userService: UserService,
              private route: ActivatedRoute) {
    this.username = this.route.snapshot.params['username'];
  };

  async ngOnInit() {
    try {
      this.dynamics = await this.userService.getFullDynamicRankingList(this.username);
    } catch (error) {
      console.error(error);
    }
  }
}
