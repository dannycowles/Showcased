package com.example.showcased.dto;

import lombok.Data;

import java.util.List;

@Data
public class ProfileDetails {
    private String username;
    private String profilePicture;
    List<WatchReturnDto> watchlistTop;
    List<WatchReturnDto> watchingTop;
    List<RankingReturnDto> showRankingTop;
    List<EpisodeRankingReturnDto> episodeRankingTop;
}
