export interface EpisodeData {
  readonly id: number;
  readonly name: string;
  readonly showTitle: string;
  readonly overview: string;
  readonly imdbRating: string;
  readonly imdbVotes: number;
  readonly runtime: number;
  readonly airDate: string;
  readonly stillPath: string;
}
