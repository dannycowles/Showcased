package com.example.showcased.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class ReviewCommentWithUserInfoDto {
    private Long id;
    private Long reviewId;
    private Long userId;
    private String username;
    private String profilePicture;
    private String commentText;
    private int numLikes;
    private Date createdAt;
    @JsonProperty("isLikedByUser")
    private boolean isLikedByUser;
    @JsonProperty("isOwnComment")
    private boolean isOwnComment;
}
