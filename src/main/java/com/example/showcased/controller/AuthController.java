package com.example.showcased.controller;

import com.example.showcased.dto.*;
import com.example.showcased.entity.User;
import com.example.showcased.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    ResponseEntity<LoginResponseDto> registerUser(@Valid @RequestBody RegisterDto registerDto) {
        LoginResponseDto registerResponse = authService.registerUser(registerDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(registerResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> loginUser(@RequestBody LoginDto loginDto) {
        LoginResponseDto loginResponse = authService.loginUser(loginDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(loginResponse);
    }

    @GetMapping("/login-status")
    public ResponseEntity<LoginStatusDto> loginStatus() {
        LoginStatusDto loginStatus = authService.loginStatus();
        return ResponseEntity.ok(loginStatus);
    }

    @PostMapping("/request-otp")
    public ResponseEntity<Void> requestOTP(@RequestBody String email) {
        authService.requestOTP(email);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/validate-otp")
    public ResponseEntity<JwtResponse> validateOTP(@RequestBody ValidateOTPDto validateOTPDto) {
        JwtResponse validateResponse = authService.validateOTP(validateOTPDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(validateResponse);
    }

    @PostMapping("/change-password")
    public ResponseEntity<Void> changePassword(@RequestBody NewPasswordDto newPasswordDto) {
        authService.changePassword(newPasswordDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

//    @PostMapping("/logout")
//    ResponseEntity<Void> logoutUser(HttpSession session) {
//        session.invalidate();
//        return ResponseEntity.noContent().build();
//    }

    @GetMapping("/check-username/{username}")
    ResponseEntity<UsernameCheckDto> checkUsernameAvailability(@PathVariable String username) {
        UsernameCheckDto userCheck = authService.checkUsernameAvailability(username);
        return ResponseEntity.ok(userCheck);
    }
}
