package com.example.showcased.controller;

import com.example.showcased.dto.LoginRegisterDto;
import com.example.showcased.dto.UserDto;
import com.example.showcased.service.LoginService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/login")
public class LoginController {

    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable("id") Long id) {
        UserDto user = loginService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @PostMapping
    public ResponseEntity<?> verifyLogin(@RequestBody LoginRegisterDto loginDto,  HttpSession session) {
        UserDto user = loginService.verifyUser(loginDto);
        // "Log" the user in by setting the session attribute
        session.setAttribute("user", user.getId());

        return ResponseEntity.ok(user);
    }
}
