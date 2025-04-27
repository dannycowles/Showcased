package com.example.showcased.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.Data;

@Entity
@Data
public class UserCharacterRanking {
    @EmbeddedId
    private UserCharacterRankingId id;
    private String showName;
    private String characterType;
    private int rankNum;
}
