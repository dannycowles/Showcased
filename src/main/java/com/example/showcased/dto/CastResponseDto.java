package com.example.showcased.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CastResponseDto {
    private List<CastDto> cast;
}
