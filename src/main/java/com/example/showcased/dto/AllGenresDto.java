package com.example.showcased.dto;

import lombok.Data;

import java.util.List;

@Data
public class AllGenresDto {
    private List<GenresDto> genres;
}
