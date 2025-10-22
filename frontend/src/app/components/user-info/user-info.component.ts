import {booleanAttribute, Component, CUSTOM_ELEMENTS_SCHEMA, HostListener, Input, OnInit} from '@angular/core';
import {NgOptimizedImage} from '@angular/common';
import {UserHeaderData} from '../../data/user-header-data';
import {UserService} from '../../services/user.service';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {UtilsService} from '../../services/utils.service';
import {RouterLink} from '@angular/router';
import {ReviewChartComponent} from '../review-chart/review-chart.component';

@Component({
  selector: 'app-user-info',
  imports: [
    NgOptimizedImage,
    ReactiveFormsModule,
    FormsModule,
    RouterLink,
    ReviewChartComponent,
  ],
  templateUrl: './user-info.component.html',
  styleUrl: './user-info.component.css',
  standalone: true,
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class UserInfoComponent implements OnInit {
  @Input({ required: true }) headerData: UserHeaderData;
  @Input({ transform: booleanAttribute }) editable: boolean = false; // true for profile page, false for user page
  socialIconSize: number = 30;

  constructor(
    private userService: UserService,
    public utilsService: UtilsService,
  ) {}

  ngOnInit() {
    this.updateIconSize();
  }

  @HostListener('window:resize')
  updateIconSize() {
    this.socialIconSize = window.innerWidth <= 600 ? 15 : 30;
  }

  async followUser() {
    try {
      const response = await this.userService.followUser(this.headerData.id);
      if (response.ok) {
        this.headerData.isFollowing = true;
        this.headerData.numFollowers += 1;
      }
    } catch (error) {
      console.error(error);
    }
  }

  async unfollowUser() {
    try {
      const response = await this.userService.unfollowUser(this.headerData.id);
      if (response.ok) {
        this.headerData.isFollowing = false;
        this.headerData.numFollowers -= 1;
      }
    } catch (error) {
      console.error(error);
    }
  }
}
