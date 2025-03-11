package com.example.showcased.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@AllArgsConstructor
@Embeddable
@NoArgsConstructor
public class LikedReviewsId implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private Long userId;
    private Long reviewId;
}
