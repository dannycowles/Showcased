import {Component, CUSTOM_ELEMENTS_SCHEMA, Input, OnInit} from '@angular/core';
import {NgOptimizedImage} from '@angular/common';
import {UserHeaderData} from '../../data/user-header-data';
import {ProfileService} from '../../services/profile.service';
import {UserService} from '../../services/user.service';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {UtilsService} from '../../services/utils.service';
import {UserSocialData} from '../../data/user-social-data';
import {RouterLink} from '@angular/router';
import {UpdateProfileDetailsDto} from '../../data/dto/update-profile-details-dto';
import {AddSocialDto} from '../../data/dto/add-social-dto';

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
export class UserInfoComponent implements OnInit {
  @Input({required: true}) headerData: UserHeaderData;
  @Input() editable: boolean = true; // true for profile page, false for user page
  @Input() userId: number;

  objectUrl: string | null = null;
  newBio: string;
  readonly bioMaxLength: number = 100;
  bioMessage: string | null = null;
  debouncedAddSocialAccount: (social: UserSocialData) => void;

  constructor(private profileService: ProfileService,
              private userService: UserService,
              public utilsService: UtilsService) { };

  ngOnInit() {
    this.newBio = this.headerData.bio;
    this.debouncedAddSocialAccount = this.utilsService.debounce((social: UserSocialData) => {
      this.addSocialAccount(social);
    });

    // Lock down editable if it's not the current user's profile to prevent malicious efforts from manual injections
    if (!this.headerData.isOwnProfile) {
      this.editable = false;
    }
  }

  get followersUrl(): string {
    return this.editable ? 'followers' : `/user/${this.userId}/followers`;
  }

  get followingUrl(): string {
    return this.editable ? 'following' : `/user/${this.userId}/following`;
  }

  async onFileSelected(event: Event) {
    const input = event.target as HTMLInputElement;
    const file: File = input.files[0];

    // Immediately show the preview image without waiting for S3
    this.objectUrl = URL.createObjectURL(file);
    this.headerData.profilePicture = this.objectUrl;

    if (file) {
      const formData: FormData = new FormData();
      formData.append('file', file);

      try {
        this.headerData.profilePicture = await this.profileService.uploadProfilePicture(formData) + "?t=" + new Date().getTime();

        // Clean up the object URL
        URL.revokeObjectURL(this.objectUrl);
      } catch(error) {
        console.error(error);
      }
    }
  }

  async updateBio() {
    // Prevent sending unchanged bio
    if (this.newBio === this.headerData.bio) {
      return;
    }

    try {
      const data: UpdateProfileDetailsDto = {
        bio: this.newBio
      };

      const response = await this.profileService.updateProfileDetails(data);
      if (response.ok) {
        this.headerData.bio = this.newBio;
        this.bioMessage = "Bio updated successfully.";
      }
    } catch (error) {
      console.error(error);
    } finally {
      setTimeout(() => {
        this.bioMessage = "";
      }, 3000);
    }
  }

  async removeSocialAccount(social: UserSocialData) {
    try {
      const response = await this.profileService.removeSocialAccount(social.socialId);
      if (response.ok) {
        social.handle = null;
      }
    } catch (error) {
      console.error(error);
    }
  }

  addSocialAccount(social: UserSocialData) {
    try {
      const data: AddSocialDto = {
        socialId: social.socialId,
        handle: social.handle
      };
      this.profileService.addSocialAccount(data);
    } catch (error) {
      console.error(error);
    }
  }


  async followUser() {
    try {
      const response = await this.userService.followUser(this.userId);
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
      const response = await this.userService.unfollowUser(this.userId);
      if (response.ok) {
        this.headerData.isFollowing = false;
        this.headerData.numFollowers -= 1;
      }
    } catch(error) {
      console.error(error);
    }
  }
}
