package com.example.showcased.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "character_info")
public class CharacterInfo {
    @Id
    private String id;
    private Long showId;
    private String name;
}
