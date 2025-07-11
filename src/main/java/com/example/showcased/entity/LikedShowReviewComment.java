package com.example.showcased.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "liked_show_review_comments")
@AllArgsConstructor
@NoArgsConstructor
public class LikedShowReviewComment {
    @EmbeddedId
    LikedShowReviewCommentId id;
}
