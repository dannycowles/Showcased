import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {UserData} from '../../../data/user-data';
import {UserService} from '../../../services/user.service';
import {UtilsService} from '../../../services/utils.service';
import {ReviewType} from '../../../data/enums';

@Component({
  selector: 'app-user-page',
  templateUrl: './user-page.component.html',
  styleUrl: './user-page.component.css',
  standalone: false
})
export class UserPageComponent implements OnInit {
  readonly userId: number;
  userDetails: UserData;
  readonly ReviewType = ReviewType;

  constructor(private route: ActivatedRoute,
              private userService: UserService,
              public utilsService: UtilsService) {
    this.userId = this.route.snapshot.params['id'];
  }

  async ngOnInit() {
    // Retrieve user details from the backend
    try {
      this.userDetails = await this.userService.getUserDetails(this.userId);
    } catch(error) {
      console.error(error);
    }
  }
}
