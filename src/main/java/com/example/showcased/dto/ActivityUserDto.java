package com.example.showcased.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ActivityUserDto {
    private Long userId;
    private String username;
    private String profilePicture;
}
