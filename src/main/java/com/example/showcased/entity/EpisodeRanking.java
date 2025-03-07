package com.example.showcased.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "user_episode_rankings")
public class EpisodeRanking {
    @EmbeddedId
    private EpisodeRankingId id;
    private Long rankNum;
}
