export interface UserSearchData {
  readonly id: number;
  readonly username: string;
  readonly profilePicture: string;
  following: boolean;
  readonly ownProfile: boolean;
}
