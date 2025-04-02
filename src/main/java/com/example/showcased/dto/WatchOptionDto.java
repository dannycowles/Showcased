package com.example.showcased.dto;

import lombok.Data;

@Data
public class WatchOptionDto {
    private String name;
    private String logoPath;

    public WatchOptionDto(String name, String logoPath) {
        this.name = name;
        setLogoPath(logoPath);
    }

    public void setLogoPath(String logoPath) {
        this.logoPath = "https://image.tmdb.org/t/p/original" + logoPath;
    }
}
