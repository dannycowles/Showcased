package com.example.showcased.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "user_show_rankings")
public class ShowRanking {
    @EmbeddedId
    private WatchId id;
    private Long rankNum;
}
