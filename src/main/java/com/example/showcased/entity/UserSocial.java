package com.example.showcased.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "user_socials")
public class UserSocial {
    @EmbeddedId
    private UserSocialId id;
    private String handle;
}
