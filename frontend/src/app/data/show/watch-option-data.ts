export class WatchOptionData {
  readonly name: string;
  readonly logoPath: string;

  constructor(jsonObject: { [key: string]: any }) {
    this.name = jsonObject['name'];
    this.logoPath = jsonObject['logoPath'];
  }
}
