export class WatchOptionData {
  name: string;
  logoPath: string;

  constructor(jsonObject: { [key: string]: any }) {
    this.name = jsonObject['name'];
    this.logoPath = jsonObject['logoPath'];
  }
}
