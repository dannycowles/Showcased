export class UserSearchData {
  readonly id: number;
  readonly username: string;
  readonly profilePicture: string;
  isFollowing: boolean;
  readonly isOwnProfile: boolean;

  constructor(jsonObject: { [key: string]: any })  {
    this.id = jsonObject['id'];
    this.username = jsonObject['username'];
    this.profilePicture = jsonObject['profilePicture'];
    this.isFollowing = jsonObject['following'];
    this.isOwnProfile = jsonObject['ownProfile'];
  }
}
