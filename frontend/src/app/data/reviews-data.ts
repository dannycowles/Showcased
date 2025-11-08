import {ReviewCommentData} from './review-comment-data';
import {PageData} from './page-data';
import {ReviewPageType} from './enums';

interface BaseReviewData {
  readonly id: number;
  readonly showId: number;
  readonly showTitle: string;
  readonly username: string;
  readonly profilePicture: string;
  readonly userId: number;
  readonly rating: number;
  readonly commentary: string;
  readonly containsSpoilers: boolean;
  numLikes: number;
  numComments: number;
  comments?: PageData<ReviewCommentData>;
  readonly reviewDate: string;
  isLikedByUser: boolean;
  isOwnReview: boolean;
  notifCommentId?: number;
}

export interface ShowReviewData extends BaseReviewData {
  readonly type: ReviewPageType.ShowPage;
}

export interface SeasonReviewData extends BaseReviewData {
  readonly type: ReviewPageType.SeasonPage;
  readonly season: number;
}

export interface EpisodeReviewData extends BaseReviewData {
  readonly type: ReviewPageType.EpisodePage;
  readonly season: number;
  readonly episode: number;
  readonly episodeTitle: string;
}
