package com.example.showcased.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class ReviewId implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private Long reviewerId;
    private Long showId;
}
