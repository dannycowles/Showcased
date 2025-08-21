package com.example.showcased.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_watchlist")
public class Watchlist {
    @EmbeddedId
    private WatchId id;
}
