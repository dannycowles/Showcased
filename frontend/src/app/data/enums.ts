export enum AddShowType {
  Ranking = "ranking list",
  Watchlist = "watchlist",
  Watching = "watching list",
  Collection = "collection"
}

export enum ReviewType {
  Show = "Show",
  Season = "Season",
  Episode = "Episode"
}

export enum ReviewPageType {
  ShowPage = "ShowPage",
  SeasonPage = "SeasonPage",
  EpisodePage = "EpisodePage"
}

export enum ActivityType {
  Follow = 1,
  LikeShowReview,
  CommentShowReview,
  LikeEpisodeReview,
  CommentEpisodeReview,
  LikeShowReviewComment,
  LikeEpisodeReviewComment,
  LikeCollection,
  LikeSeasonReview,
  CommentSeasonReview,
  LikeSeasonReviewComment
}
