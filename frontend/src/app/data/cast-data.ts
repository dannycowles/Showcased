export class CastData {
  id: number;
  name: string;
  character: string;
  profilePath: string;

  constructor(jsonObject: { [key: string]: any }) {
    this.id = jsonObject['id'];
    this.name = jsonObject['name'];
    this.character = jsonObject['character'];

    if (jsonObject['profile_path'] === "default") {
      this.profilePath = "no-picture.svg";
    } else {
      this.profilePath = jsonObject['profile_path'];
    }
  }
}
