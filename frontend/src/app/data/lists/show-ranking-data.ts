export class ShowRankingData {
  readonly showId: number;
  rankNum: number;
  readonly title: string;
  readonly posterPath: string;

  constructor(jsonObject: { [key : string]: any }) {
    this.showId = jsonObject['showId'];
    this.rankNum = jsonObject['rankNum'];
    this.title = jsonObject['title'];
    this.posterPath = jsonObject['posterPath'];
  }
}
