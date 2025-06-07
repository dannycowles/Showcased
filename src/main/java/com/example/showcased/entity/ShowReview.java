package com.example.showcased.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Data
@Table(name = "show_reviews")
public class ShowReview {
    private Long id;
    @EmbeddedId
    private ShowReviewId key;
    private double rating;
    private String commentary;
    private boolean containsSpoilers;
    private Long numLikes = 0L;
    private Date reviewDate = new Date();
}
