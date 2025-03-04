package com.example.showcased.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class ReviewWithUserInfoDto {
    private Long id;
    private String username;
    private Long reviewerId;
    private Long showId;
    private double rating;
    private String commentary;
    private boolean containsSpoilers;
    private Long numLikes;
    private Date reviewDate;
}
