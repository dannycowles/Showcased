package com.example.showcased.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.Data;

@Entity
@Data
public class SeasonRanking {
    @EmbeddedId
    private SeasonRankingId id;
    private Long rankNum;
    private String posterPath;
}
