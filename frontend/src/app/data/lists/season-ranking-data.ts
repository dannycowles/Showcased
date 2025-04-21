export class SeasonRankingData {
  readonly id: number;
  readonly showId: number;
  readonly season: number;
  rankNum: number;
  readonly posterPath: string;
  readonly showTitle: string;

  constructor(jsonObject: { [key : string]: any }) {
    this.id = jsonObject['id'];
    this.showId = jsonObject['showId'];
    this.season = jsonObject['season'];
    this.rankNum = jsonObject['rankNum'];
    this.posterPath = jsonObject['posterPath'];
    this.showTitle = jsonObject['showTitle'];
  }
}
