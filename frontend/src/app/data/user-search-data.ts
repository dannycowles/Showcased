export interface UserSearchData {
  readonly id: number;
  readonly displayName: string;
  readonly profilePicture: string;
  isFollowing: boolean;
  readonly isOwnProfile: boolean;
}
