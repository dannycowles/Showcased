package com.example.showcased.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@Table(name = "episode_info")
public class EpisodeInfo {
    @Id
    private Long id;
    private Long showId;
    private String showTitle;
    private String episodeTitle;
    private int season;
    private int episode;
    private String posterPath;
}
