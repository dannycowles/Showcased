package com.example.showcased.controller;

import com.example.showcased.dto.LoginRegisterDto;
import com.example.showcased.dto.UserDto;
import com.example.showcased.service.RegisterService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/register")
public class RegisterController {

    private final RegisterService registerService;

    public RegisterController(RegisterService registerService) {
        this.registerService = registerService;
    }

    @PostMapping
    ResponseEntity<UserDto> registerUser(@RequestBody LoginRegisterDto registerDto) {
        UserDto user = registerService.registerUser(registerDto);
        return ResponseEntity.ok(user);
    }

}
