package com.example.showcased.dto;

import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class RegisterDto {
    @Email(message = "Email is not valid", regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
    private String email;
    private String username;
    private String password;
    private String recaptcha;
}
