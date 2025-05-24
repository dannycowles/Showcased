package com.example.showcased.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "shows_in_collections")
public class ShowsInCollection {
    @EmbeddedId
    private ShowsInCollectionId id;
    private Long rankNum;
}
