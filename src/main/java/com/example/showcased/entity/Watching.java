package com.example.showcased.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "currently_watching")
public class Watching {
    @EmbeddedId
    private WatchId id;
}
