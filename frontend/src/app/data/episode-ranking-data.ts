export class EpisodeRankingData {
  showId: number;
  rankNum: number;
  showTitle: string;
  episodeTitle: string;
  season: number;
  episode: number;
  posterPath: string;

  constructor(jsonObject: { [key: string]: any }) {
    this.showId = jsonObject['showId'];
    this.rankNum = jsonObject['rankNum'];
    this.showTitle = jsonObject['showTitle'];
    this.episodeTitle = jsonObject['episodeTitle'];
    this.season = jsonObject['season'];
    this.episode = jsonObject['episode'];
    this.posterPath = jsonObject['poster_path'];
  }
}
