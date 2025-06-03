package com.example.showcased.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "social_platforms")
public class SocialPlatform {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String url;
    private String icon;
}
