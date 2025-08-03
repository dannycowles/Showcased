import {ReviewType} from './enums';

export interface ProfileShowReviewData {
  readonly type: ReviewType;
  readonly reviewId: number;
  readonly showId: number;
  readonly rating: number;
  readonly showTitle: string;
  readonly commentary: string;
  readonly containsSpoilers: boolean;
  readonly posterPath: string;
  readonly reviewDate: string;
  numLikes: number;
  isLikedByUser: boolean;
}

export interface ProfileEpisodeReviewData extends ProfileShowReviewData {
  readonly episodeTitle: string;
  readonly season: number;
  readonly episode: number;
}
