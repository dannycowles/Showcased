import {ReviewType} from './enums';

export interface ProfileShowReviewData {
  readonly type: ReviewType;
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

export interface ProfileEpisodeReviewData extends ProfileShowReviewData {
  readonly episodeTitle: string;
  readonly season: number;
  readonly episode: number;
}
