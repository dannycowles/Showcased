package com.example.showcased.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class ActivityDto {
    private Long id;
    private int activityType;
    private String description;
    private ActivityUserDto follow;
    private ActivityShowReviewLikeDto showReviewLike;
    private ActivityShowReviewCommentDto showReviewComment;
    private Date createdAt;
}
