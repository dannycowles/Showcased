export class UserSearchData {
  id: number;
  username: string;
  profilePicture: string;

  constructor(jsonObject: { [key: string]: any })  {
    this.id = jsonObject['id'];
    this.username = jsonObject['username'];
    this.profilePicture = jsonObject['profilePicture'];
  }
}
