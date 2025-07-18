package com.example.showcased.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "activities")
public class Activity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private int activityType;
    private Long externalId;
}
