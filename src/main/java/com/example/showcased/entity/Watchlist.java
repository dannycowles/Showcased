package com.example.showcased.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "user_watchlist")
public class Watchlist {
    @EmbeddedId
    private WatchlistId id;
}
