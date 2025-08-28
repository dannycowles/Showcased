package com.example.showcased.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
@Entity
@Table(name = "users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String displayName;
    private String password;
    private String profilePicture;
    private String bio;
    private int numFollowers;
    private int numFollowing;
    private int numReviews;

    public User(String email, String username, String password) {
        this.email = email;
        this.displayName = username;
        this.password = password;
        this.profilePicture = "https://showcased-9bbb23e3-216f-4384-b375-4e567ba0d530.s3.us-west-2.amazonaws.com/users/default-pfp";
    }

    public User() {}

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
