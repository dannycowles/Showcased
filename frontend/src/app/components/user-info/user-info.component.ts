import {booleanAttribute, Component, CUSTOM_ELEMENTS_SCHEMA, Input} from '@angular/core';
import {NgOptimizedImage} from '@angular/common';
import {UserHeaderData} from '../../data/user-header-data';
import {UserService} from '../../services/user.service';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {UtilsService} from '../../services/utils.service';
import {RouterLink} from '@angular/router';

@Component({
  selector: 'app-user-info',
  imports: [
    NgOptimizedImage,
    ReactiveFormsModule,
    FormsModule,
    RouterLink,
  ],
  templateUrl: './user-info.component.html',
  styleUrl: './user-info.component.css',
  standalone: true,
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class UserInfoComponent {
  @Input({required: true}) headerData: UserHeaderData;
  @Input({transform: booleanAttribute}) editable: boolean = false; // true for profile page, false for user page

  constructor(private userService: UserService,
              public utilsService: UtilsService) { };


  get followersUrl(): string {
    return this.headerData.isOwnProfile ? 'followers' : `/user/${this.headerData.displayName}/followers`;
  }

  get followingUrl(): string {
    return this.headerData.isOwnProfile ? 'following' : `/user/${this.headerData.displayName}/following`;
  }

  async followUser() {
    try {
      const response = await this.userService.followUser(this.headerData.id);
      if (response.ok) {
        this.headerData.isFollowing = true;
        this.headerData.numFollowers += 1;
      }
    } catch(error) {
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
    } catch(error) {
      console.error(error);
    }
  }
}
