package com.example.showcased.controller;

import com.example.showcased.dto.LoginDto;
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
