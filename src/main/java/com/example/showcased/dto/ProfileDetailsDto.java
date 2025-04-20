package com.example.showcased.dto;

import lombok.Data;

import java.util.List;

@Data
public class ProfileDetailsDto {
    private String username;
    private String profilePicture;
    private List<WatchReturnDto> watchlistTop;
    private boolean moreWatchlist = false;
    private List<WatchReturnDto> watchingTop;
    boolean moreWatching = false;
    private List<RankingReturnDto> showRankingTop;
    private boolean moreShowRanking = false;
    private List<EpisodeRankingReturnDto> episodeRankingTop;
    private boolean moreEpisodeRanking = false;
    private List<ReviewWithUserInfoDto> reviews;
    private Long numFollowers;
    private Long numFollowing;
    private boolean isFollowing = false;
}
