package com.example.showcased.dto;

import lombok.Data;

import java.util.List;

@Data
public class ProfileDetailsDto {
    private String username;
    private String profilePicture;
    List<WatchReturnDto> watchlistTop;
    List<WatchReturnDto> watchingTop;
    List<RankingReturnDto> showRankingTop;
    List<EpisodeRankingReturnDto> episodeRankingTop;
    List<ReviewWithUserInfoDto> reviews;
}
