package com.example.showcased.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserSearchDto {
    private Long id;
    private String username;
    private String profilePicture;
    @JsonProperty("isFollowing")
    private boolean isFollowing = false;
    @JsonProperty("isOwnProfile")
    private boolean isOwnProfile = false;
}
