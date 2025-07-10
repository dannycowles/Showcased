export interface EpisodeData {
  readonly id: number;
  readonly episodeTitle: string;
  readonly plot: string;
  readonly showTitle: string;
  readonly imdbRating: string;
  readonly imdbVotes: number;
  readonly runtime: number;
  readonly airDate: string;
  readonly stillPath: string;
  isOnRankingList: boolean;
}
