package com.example.showcased.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "user_dynamics_rankings")
public class DynamicRanking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    @Column(name = "character1_id")
    private String character1Id;

    @Column(name = "character2_id")
    private String character2Id;

    private int rankNum;
}
