package com.example.showcased.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ActivityEpisodeReviewLikeDto {
    private Long reviewId;
    private Long showId;
    private String showTitle;
    private Integer season;
    private Integer episode;
    private String episodeTitle;
}
