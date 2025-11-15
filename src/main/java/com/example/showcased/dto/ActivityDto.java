package com.example.showcased.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ActivityDto {
    private Long id;
    private int activityType;
    private String description;
    private ActivityUserDto user;
    private ActivityShowReviewLikeDto showReviewLike;
    private ActivityShowReviewCommentDto showReviewComment;
    private ActivityEpisodeReviewLikeDto episodeReviewLike;
    private ActivityEpisodeReviewCommentDto episodeReviewComment;
    private ActivityShowReviewCommentLikeDto showReviewCommentLike;
    private ActivityEpisodeReviewCommentLikeDto episodeReviewCommentLike;
    private ActivityCollectionLikeDto collectionLike;
    private ActivitySeasonReviewLikeDto seasonReviewLike;
    private ActivitySeasonReviewCommentDto seasonReviewComment;
    private ActivitySeasonReviewCommentLikeDto seasonReviewCommentLike;
    private Date createdAt;
}
