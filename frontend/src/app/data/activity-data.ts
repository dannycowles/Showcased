import {ActivityType} from './enums';

export interface ActivityData {
  readonly id: number;
  readonly activityType: ActivityType;
  readonly description: string;
  readonly user: ActivityUserData;
  readonly showReviewLike: ActivityShowReviewLikeData;
  readonly showReviewComment: ActivityShowReviewCommentData;
  readonly episodeReviewLike: ActivityEpisodeReviewLikeData;
  readonly episodeReviewComment: ActivityEpisodeReviewCommentData;
  readonly showReviewCommentLike: ActivityShowReviewCommentLikeData;
  readonly episodeReviewCommentLike: ActivityEpisodeReviewCommentLikeData;
  readonly collectionLike: ActivityCollectionLikeData;
  readonly createdAt: string;
}

interface ActivityUserData {
  readonly userId: number;
  readonly username: string;
  readonly profilePicture: string;
}

interface CommentData {
  readonly commentId: number;
  readonly commentText: string;
}

interface CommentLikeData {
  readonly reviewUser: ActivityUserData;
  readonly isOwnComment: boolean;
}

interface ActivityShowReviewLikeData {
  readonly reviewId: number;
  readonly showId: number;
  readonly showTitle: string;
}

interface ActivityEpisodeReviewLikeData extends ActivityShowReviewLikeData {
  readonly season: number;
  readonly episode: number;
  readonly episodeTitle: string;
}

interface ActivityShowReviewCommentData extends ActivityShowReviewLikeData, CommentData {}

interface ActivityEpisodeReviewCommentData extends ActivityEpisodeReviewLikeData, CommentData {}

interface ActivityShowReviewCommentLikeData extends ActivityShowReviewLikeData, CommentData, CommentLikeData {}

interface ActivityEpisodeReviewCommentLikeData extends ActivityEpisodeReviewLikeData, CommentData, CommentLikeData {}

interface ActivityCollectionLikeData {
  readonly collectionId: number;
  readonly collectionName: string;
}
