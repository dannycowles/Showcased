package com.example.showcased.dto;

import lombok.Data;

import java.util.List;

@Data
public class ProfileDetailsDto {
    private UserHeaderDataDto headerData;

    private List<WatchReturnDto> watchlistTop;
    private List<WatchReturnDto> watchingTop;
    private List<RankingReturnDto> showRankingTop;
    private List<EpisodeRankingReturnDto> episodeRankingTop;
    private List<SeasonRankingReturnDto> seasonRankingTop;
    private AllCharacterRankingDto characterRankings;

    private boolean hasMoreWatchlist = false;
    private boolean hasMoreWatching = false;
    private boolean hasMoreShowRanking = false;
    private boolean hasMoreSeasonRanking = false;
    private boolean hasMoreEpisodeRanking = false;

    private List<ShowReviewWithUserInfoDto> showReviews;
    private List<EpisodeReviewWithUserInfoDto> episodeReviews;
}
