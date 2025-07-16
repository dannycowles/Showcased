package com.example.showcased.dto;

import lombok.Data;

@Data
public class WatchSendDto {
    private Long userId;
    private Long showId;
    private String showTitle;
    private String posterPath;
}
