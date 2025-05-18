package com.example.showcased.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WatchReturnDto {
    private Long showId;
    private String title;
    private String posterPath;
}
