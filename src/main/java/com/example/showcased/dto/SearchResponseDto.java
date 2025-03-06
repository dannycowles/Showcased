package com.example.showcased.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SearchResponseDto {
    private List<SearchDto> results;
}
