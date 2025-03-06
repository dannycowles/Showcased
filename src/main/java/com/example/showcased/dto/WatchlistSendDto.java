package com.example.showcased.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WatchlistSendDto {
    private Long userId;
    private Long showId;
    private String title;
    private String posterPath;
}
