package com.example.showcased.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String username;
    private String password;
    private String profilePicture;
    private String bio;

    public User(String email, String username, String password) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.profilePicture = "https://showcased-9bbb23e3-216f-4384-b375-4e567ba0d530.s3.us-west-2.amazonaws.com/users/default-pfp";
    }

    public User() {}

}
