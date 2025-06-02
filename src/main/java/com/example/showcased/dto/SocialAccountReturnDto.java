package com.example.showcased.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SocialAccountReturnDto {
    private Long socialId;
    private String socialName;
    private String handle;
}
