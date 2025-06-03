import {UserSocialData} from './user-social-data';

export interface UserHeaderData {
  readonly username: string;
  bio: string;
  profilePicture: string;
  numFollowers: number;
  numFollowing: number;
  socials: UserSocialData[];
  isFollowing: boolean;
  isOwnProfile: boolean;
}
