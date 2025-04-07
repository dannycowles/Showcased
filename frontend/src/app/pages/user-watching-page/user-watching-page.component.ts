import {Component, OnInit} from '@angular/core';
import {UserService} from '../../services/user.service';

@Component({
  selector: 'app-user-watching-page',
  templateUrl: './user-watching-page.component.html',
  styleUrl: './user-watching-page.component.css',
  standalone: false
})
export class UserWatchingPageComponent implements OnInit {

  constructor(private userService: UserService) { };

  ngOnInit() {};

}
