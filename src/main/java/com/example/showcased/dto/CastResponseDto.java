package com.example.showcased.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CastResponseDto {
    private List<CastDto> cast;

    public void setCast(List<CastDto> cast) {
        // Retrieve only the first 5 cast members
        this.cast =  cast.stream().limit(5).toList();
    }
}
