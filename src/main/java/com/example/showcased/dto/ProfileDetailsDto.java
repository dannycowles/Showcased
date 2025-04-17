package com.example.showcased.dto;

import lombok.Data;

import java.util.List;

@Data
public class ProfileDetailsDto {
    private String username;
    private String profilePicture;
    List<WatchReturnDto> watchlistTop;
    boolean moreWatchlist = false;
    List<WatchReturnDto> watchingTop;
    boolean moreWatching = false;
    List<RankingReturnDto> showRankingTop;
    boolean moreShowRanking = false;
    List<EpisodeRankingReturnDto> episodeRankingTop;
    boolean moreEpisodeRanking = false;
    List<ReviewWithUserInfoDto> reviews;
    private Long numFollowers;
    private Long numFollowing;
}
