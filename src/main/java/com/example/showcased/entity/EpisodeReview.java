package com.example.showcased.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Data
@Table(name = "episode_reviews")
public class EpisodeReview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private Long episodeId;
    private double rating;
    private String commentary;
    private boolean containsSpoilers;
    private Long numLikes = 0L;
    private int numComments = 0;
    private Date reviewDate = new Date();
}
