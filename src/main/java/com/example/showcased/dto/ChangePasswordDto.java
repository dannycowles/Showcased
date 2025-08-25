package com.example.showcased.dto;

import lombok.Data;

@Data
public class ChangePasswordDto {
    private String currentPassword;
    private String newPassword;
}
