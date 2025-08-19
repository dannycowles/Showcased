import {UserSocialData} from './user-social-data';

export interface ProfileSettingsData {
  readonly displayName: string;
  readonly email: string;
  bio: string;
  profilePicture: string;
  readonly socialAccounts: UserSocialData[];
}
