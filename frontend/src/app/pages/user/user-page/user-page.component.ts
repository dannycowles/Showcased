import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {ProfileData} from '../../../data/profile-data';
import {UserService} from '../../../services/user.service';
import {UtilsService} from '../../../services/utils.service';
import {UserHeaderData} from '../../../data/user-header-data';

@Component({
  selector: 'app-user-page',
  templateUrl: './user-page.component.html',
  styleUrl: './user-page.component.css',
  standalone: false
})
export class UserPageComponent implements OnInit {
  readonly userId: number;
  userDetails: ProfileData;
  headerData: UserHeaderData;

  constructor(private route: ActivatedRoute,
              private userService: UserService,
              public utilsService: UtilsService) {
    this.userId = this.route.snapshot.params['id'];
  }

  async ngOnInit() {
    // Retrieve user details from the backend
    try {
      this.userDetails = await this.userService.getUserDetails(this.userId);
      this.headerData = this.userDetails.userHeaderData;
    } catch(error) {
      console.error(error);
    }
  }
}
