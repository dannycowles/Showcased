export class ShowRankingData {
  showId: number;
  rankNum: number;
  title: string;
  posterPath: string;

  constructor(jsonObject: { [key : string]: any }) {
    this.showId = jsonObject['showId'];
    this.rankNum = jsonObject['rankNum'];
    this.title = jsonObject['title'];
    this.posterPath = jsonObject['posterPath'];
  }
}
