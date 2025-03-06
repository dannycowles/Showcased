package com.example.showcased.controller;

import com.example.showcased.dto.LoginRegisterDto;
import com.example.showcased.dto.UserDto;
import com.example.showcased.service.AuthService;
import jakarta.servlet.http.HttpSession;
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
    public ResponseEntity<UserDto> loginUser(@RequestBody LoginRegisterDto loginDto, HttpSession session) {
        UserDto user = authService.loginUser(loginDto, session);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/register")
    ResponseEntity<UserDto> registerUser(@RequestBody LoginRegisterDto registerDto) {
        UserDto user = authService.registerUser(registerDto);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/logout")
    ResponseEntity<Void> logoutUser(HttpSession session) {
        session.invalidate();
        return ResponseEntity.noContent().build();
    }
}
