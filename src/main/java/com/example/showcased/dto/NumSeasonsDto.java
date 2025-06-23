package com.example.showcased.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NumSeasonsDto {
    @JsonAlias("number_of_seasons")
    private int numSeasons;
}
