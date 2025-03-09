package com.example.showcased.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@Table(name = "episode_info")
public class EpisodeInfo {
    @EmbeddedId
    private EpisodeInfoId id;
    private String showTitle;
    private String episodeTitle;
    private String posterPath;
}
