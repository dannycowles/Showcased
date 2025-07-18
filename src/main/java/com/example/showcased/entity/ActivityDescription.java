package com.example.showcased.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "activity_descriptions")
public class ActivityDescription {
    @Id
    private int activityType;
    private String description;
}
