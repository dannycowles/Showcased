package com.example.showcased.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name = "otp_requests")
public class OtpRequest {
    @Id
    private Long userId;
    private String otp;
    private Date expiresAt;
}
