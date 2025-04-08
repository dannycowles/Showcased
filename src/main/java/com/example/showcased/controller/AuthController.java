package com.example.showcased.controller;

import com.example.showcased.dto.LoginDto;
import com.example.showcased.dto.LoginStatusDto;
import com.example.showcased.dto.RegisterDto;
import com.example.showcased.dto.UsernameCheckDto;
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

    @PostMapping("/login")
    public ResponseEntity<Void> loginUser(@RequestBody LoginDto loginDto, HttpSession session) {
        authService.loginUser(loginDto, session);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/login-status")
    public ResponseEntity<LoginStatusDto> loginStatus(HttpSession session) {
        return ResponseEntity.ok(authService.loginStatus(session));
    }

    /* TODO: change to POST, this endpoint should take in a DTO with email
        then check users table and see if email exists, if so we generate OTP
        send user email with that OTP and store OTP details in database table
        database table otp_requests should have:
        - ID of user OTP was generated for as PK
        - OTP itself
        - expiration time
    */
    @PostMapping("/request-otp")
    public ResponseEntity<Void> requestOTP(@RequestBody String email) {
        authService.requestOTP(email);
        return ResponseEntity.ok().build();
    }

    /*
        TODO: change to POST, this endpoint should take in a DTO with email and OTP
         then checks the otp_requests database. if the credentials match return success
         message and delete the entry. if the OTP has expired return message letting them know
         the code is expired and delete the entry. if the credentials are wrong return error message
     */
    @GetMapping("/validate-otp")
    public ResponseEntity<Void> validateOTP(HttpSession session) {
        return ResponseEntity.ok().build();
    }

    @PostMapping("/register")
    ResponseEntity<Void> registerUser(@Valid @RequestBody RegisterDto registerDto) {
        authService.registerUser(registerDto);
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
