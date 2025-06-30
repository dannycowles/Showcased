export interface SeasonEpisode {
  readonly id: number;
  readonly episodeNumber: number;
  readonly name: string;
  readonly stillPath: string;
}

export interface SeasonEpisodeFull extends SeasonEpisode {
  readonly plot: string;
  readonly imdbRating: string;
  readonly airDate: Date;
}

export interface SeasonEpisodes {
  readonly episodes: SeasonEpisode[];
}
