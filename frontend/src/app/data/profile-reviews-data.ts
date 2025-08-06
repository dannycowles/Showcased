import {ReviewType} from './enums';

export interface BaseProfileReviewData {
  readonly id: number;
  readonly showId: number;
  rating: number;
  readonly showTitle: string;
  commentary: string;
  containsSpoilers: boolean;
  readonly posterPath: string;
  readonly reviewDate: string;
  numLikes: number;
  isLikedByUser: boolean;
}

export interface ProfileShowReviewData extends BaseProfileReviewData {
  readonly type: ReviewType.Show;
}

export interface ProfileEpisodeReviewData extends BaseProfileReviewData {
  readonly type: ReviewType.Episode;
  readonly episodeTitle: string;
  readonly season: number;
  readonly episode: number;
}
