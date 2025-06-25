export interface UserSearchData {
  readonly id: number;
  readonly username: string;
  readonly profilePicture: string;
  isFollowing: boolean;
  readonly isOwnProfile: boolean;
}
