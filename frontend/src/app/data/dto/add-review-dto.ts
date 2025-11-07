export interface BaseAddReviewDto {
  rating: number;
  showTitle: string;
  commentary: string;
  containsSpoilers: boolean;
  posterPath: string;
}

export interface AddShowReviewDto extends BaseAddReviewDto {}

export interface AddSeasonReviewDto extends BaseAddReviewDto {
  showId: number;
  season: number;
}

export interface AddEpisodeReviewDto extends BaseAddReviewDto {
  showId: number;
  episodeTitle: string;
  season: number;
  episode: number;
}
