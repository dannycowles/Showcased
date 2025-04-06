package com.example.showcased.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Data
@Table(name = "reviews")
public class Review {
    @EmbeddedId
    private ReviewId id;
    private String showTitle;
    private Long reviewId;
    private double rating;
    private String commentary;
    private boolean containsSpoilers;
    private Long numLikes = 0L;
    private Date reviewDate = new Date();
}
