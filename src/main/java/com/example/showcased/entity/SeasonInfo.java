package com.example.showcased.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "season_info")
public class SeasonInfo {
    @Id
    private Long id;
    private Long showId;
    private int season;
    private String posterPath;
    private String showTitle;
}
