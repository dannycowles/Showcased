package com.example.showcased.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "reviews")
public class Review {
    @EmbeddedId
    private ReviewId id;
    private Long reviewId;
    private double rating;
    private String commentary;
    private boolean containsSpoilers;
    private Long numLikes = 0L;
    private Date reviewDate = new Date();
}
