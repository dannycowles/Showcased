package com.example.showcased.entity;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class UserCharacterRankingId {
    private Long userId;
    private String characterName;
}
