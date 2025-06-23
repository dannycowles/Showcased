export interface EpisodeRankingData {
  readonly id: number;
  readonly showId: number;
  rankNum: number;
  readonly showTitle: string;
  readonly episodeTitle: string;
  readonly season: number;
  readonly episode: number;
  readonly posterPath: string;
}
