package com.example.showcased.dto;

import lombok.Data;

@Data
public class ValidateOTPDto {
    private String email;
    private String otp;
}
