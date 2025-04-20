package com.example.showcased.dto;

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
    private boolean isFollowing = false;
    private boolean isOwnProfile = false;
}
