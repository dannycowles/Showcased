export class CreatorData {
  id: number;
  name: string;
  profilePath: string;

  constructor(jsonObject: { [key: string]: any }) {
    this.id = jsonObject['id'];
    this.name = jsonObject['name'];

    if (jsonObject['profile_path'] === "default") {
      this.profilePath = "no-picture.svg";
    } else {
      this.profilePath = jsonObject['profile_path'];
    }
  }
}
