export class EpisodeRankingData {
  readonly id: number;
  readonly showId: number;
  rankNum: number;
  readonly showTitle: string;
  readonly episodeTitle: string;
  readonly season: number;
  readonly episode: number;
  readonly posterPath: string;

  constructor(jsonObject: { [key: string]: any }) {
    this.id = jsonObject['id'];
    this.showId = jsonObject['showId'];
    this.rankNum = jsonObject['rankNum'];
    this.showTitle = jsonObject['showTitle'];
    this.episodeTitle = jsonObject['episodeTitle'];
    this.season = jsonObject['season'];
    this.episode = jsonObject['episode'];
    this.posterPath = jsonObject['posterPath'];
  }
}
