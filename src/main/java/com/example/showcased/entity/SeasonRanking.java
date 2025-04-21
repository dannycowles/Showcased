package com.example.showcased.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "user_season_rankings")
public class SeasonRanking {
    @EmbeddedId
    private SeasonRankingId id;
    private Long rankNum;
}
