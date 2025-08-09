package com.example.showcased.controller;

import com.example.showcased.dto.*;
import com.example.showcased.entity.User;
import com.example.showcased.service.AuthService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
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
    ResponseEntity<User> registerUser(@Valid @RequestBody RegisterDto registerDto) {
        User registeredUser = authService.registerUser(registerDto);
        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginUser(@RequestBody LoginDto loginDto) {
        LoginResponse loginResponse = authService.loginUser(loginDto);
        return ResponseEntity.ok(loginResponse);
    }

    @GetMapping("/login-status")
    public ResponseEntity<LoginStatusDto> loginStatus(HttpSession session) {
        return ResponseEntity.ok(authService.loginStatus(session));
    }

    @PostMapping("/request-otp")
    public ResponseEntity<Void> requestOTP(@RequestBody String email) {
        authService.requestOTP(email);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/validate-otp")
    public ResponseEntity<Void> validateOTP(@RequestBody ValidateOTPDto validateOTPDto, HttpSession session) {
        authService.validateOTP(validateOTPDto, session);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/change-password")
    public ResponseEntity<Void> changePassword(@RequestBody NewPasswordDto newPasswordDto, HttpSession session) {
        authService.changePassword(newPasswordDto, session);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/logout")
    ResponseEntity<Void> logoutUser(HttpSession session) {
        session.invalidate();
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/check-username/{username}")
    ResponseEntity<UsernameCheckDto> checkUsernameAvailability(@PathVariable String username) {
        UsernameCheckDto userCheck = authService.checkUsernameAvailability(username);
        return ResponseEntity.ok(userCheck);
    }
}
