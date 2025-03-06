package com.example.showcased.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class WatchReturnDto {
    private Long showId;
    private String title;
    private String posterPath;
}
