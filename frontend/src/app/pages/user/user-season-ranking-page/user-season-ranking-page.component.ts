import {Component, OnInit} from '@angular/core';
import {UserService} from '../../../services/user.service';
import {ActivatedRoute} from '@angular/router';

@Component({
  selector: 'app-user-season-ranking-page',
  templateUrl: './user-season-ranking-page.component.html',
  styleUrl: './user-season-ranking-page.component.css',
  standalone: false
})
export class UserSeasonRankingPageComponent implements OnInit {

  constructor(private userService: UserService,
              private route: ActivatedRoute) { };

  ngOnInit() {};

}
