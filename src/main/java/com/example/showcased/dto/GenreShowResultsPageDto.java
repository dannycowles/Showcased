package com.example.showcased.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class GenreShowResultsPageDto extends ShowResultsPageDto {
    private String genre;
}
