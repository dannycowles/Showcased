package com.example.showcased.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name = "season_reviews")
public class SeasonReview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private Long showId;
    private Long seasonId;
    private double rating;
    private String commentary;
    private boolean containsSpoilers;
    private Long numLikes = 0L;
    private int numComments = 0;
    private Date reviewDate = new Date();
}
