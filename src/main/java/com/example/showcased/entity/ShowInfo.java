package com.example.showcased.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "show_info")
public class ShowInfo {
    @Id
    private Long showId;
    private String title;
    private String posterPath;
}
