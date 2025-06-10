package com.example.showcased.dto;

import lombok.Data;

import java.util.List;

@Data
public class ProfileDetailsDto {
    private String username;
    private String profilePicture;
    private String bio;
    private List<SocialAccountReturnDto> socialAccounts;
    private Long numFollowers;
    private Long numFollowing;

    private List<WatchReturnDto> watchlistTop;
    private List<WatchReturnDto> watchingTop;
    private List<RankingReturnDto> showRankingTop;
    private List<EpisodeRankingReturnDto> episodeRankingTop;
    private List<SeasonRankingReturnDto> seasonRankingTop;
    private AllCharacterRankingDto characterRankings;

    private boolean moreWatchlist = false;
    private boolean moreWatching = false;
    private boolean moreShowRanking = false;
    private boolean moreSeasonRanking = false;
    private boolean moreEpisodeRanking = false;

    private List<ShowReviewWithUserInfoDto> showReviews;
    private List<EpisodeReviewWithUserInfoDto> episodeReviews;

    private boolean isFollowing = false;
    private boolean isOwnProfile = false;
}
