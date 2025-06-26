import {UserSocialData} from './user-social-data';

export interface UserHeaderData {
  readonly username: string;
  bio: string;
  profilePicture: string;
  numFollowers: number;
  numFollowing: number;
  socialAccounts: UserSocialData[];
  isFollowing: boolean;
  isOwnProfile: boolean;
}
