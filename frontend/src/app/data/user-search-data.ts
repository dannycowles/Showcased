export class UserSearchData {
  readonly id: number;
  readonly username: string;
  readonly profilePicture: string;

  constructor(jsonObject: { [key: string]: any })  {
    this.id = jsonObject['id'];
    this.username = jsonObject['username'];
    this.profilePicture = jsonObject['profilePicture'];
  }
}
