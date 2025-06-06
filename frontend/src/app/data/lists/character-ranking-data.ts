export class CharacterRankingData {
  id: string;
  showId: number;
  name: string;
  showName: string;
  rankNum: number;

  constructor(jsonObject: {[key: string]: any}) {
    this.id = jsonObject['id'];
    this.showId = jsonObject['showId'];
    this.name = jsonObject['characterName'];
    this.showName = jsonObject['showName'];
    this.rankNum = jsonObject['rankNum'];
  }
}
