package com.example.showcased.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class LoginResponseDto extends JwtResponse {
    private String username;
    private String profilePicture;

    public LoginResponseDto(String token, long expiresIn, String username, String profilePicture) {
        super(token, expiresIn);
        this.username = username;
        this.profilePicture = profilePicture;
    }
}
