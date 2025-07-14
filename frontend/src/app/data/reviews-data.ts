import {ReviewCommentData} from './review-comment-data';

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
  comments?: ReviewCommentData[];
  readonly reviewDate: string;
  isLikedByUser: boolean;
  isLoadingComments?: boolean;
  areCommentsHidden?: boolean;
}

export interface ShowReviewData extends BaseReviewData { }

export interface EpisodeReviewData extends BaseReviewData {
  readonly season: number;
  readonly episode: number;
  readonly episodeTitle: string;
}
