package com.example.showcased.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "user_character_rankings")
public class CharacterRanking {
    @EmbeddedId
    private CharacterRankingId id;
    private String characterType;
    private int rankNum;
}
