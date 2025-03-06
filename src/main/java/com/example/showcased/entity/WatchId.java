package com.example.showcased.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class WatchId implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private Long userId;
    private Long showId;
}
