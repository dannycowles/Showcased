export enum AddShowType {
  Ranking = "ranking list",
  Watchlist = "watchlist",
  Watching = "watching list",
  Collection = "collection"
}

export enum ReviewType {
  Show = "Show",
  Episode = "Episode",
}

export enum ActivityType {
  Follow = 1,
  LikeShowReview,
  CommentShowReview,
  LikeEpisodeReview,
  CommentEpisodeReview,
  LikeShowReviewComment,
  LikeEpisodeReviewComment,
  LikeCollection
}
