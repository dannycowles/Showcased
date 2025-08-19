package com.example.showcased.dto;

import lombok.Data;

import java.util.List;

@Data
public class ProfileSettingsDto {
    private String displayName;
    private String email;
    private String bio;
    private String profilePicture;
    private List<SocialAccountReturnDto> socialAccounts;
}
