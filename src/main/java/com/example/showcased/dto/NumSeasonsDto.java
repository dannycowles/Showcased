package com.example.showcased.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NumSeasonsDto {
    @JsonProperty("number_of_seasons")
    private int numSeasons;
}
