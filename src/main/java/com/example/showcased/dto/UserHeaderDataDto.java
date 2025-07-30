package com.example.showcased.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class UserHeaderDataDto {
    private String username;
    private String profilePicture;
    private String bio;
    private List<SocialAccountReturnDto> socialAccounts;
    private int numFollowers;
    private int numFollowing;
    @JsonProperty("isFollowing")
    private boolean isFollowing = false;
    @JsonProperty("isOwnProfile")
    private boolean isOwnProfile = false;
}
