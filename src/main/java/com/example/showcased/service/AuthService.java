package com.example.showcased.service;

import com.example.showcased.dto.LoginRegisterDto;
import com.example.showcased.dto.UserDto;
import com.example.showcased.entity.User;
import com.example.showcased.exception.InvalidLoginException;
import com.example.showcased.exception.UsernameTakenException;
import com.example.showcased.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public AuthService(UserRepository userRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    // Function that verifies that the login credentials are valid
    public UserDto loginUser(LoginRegisterDto loginDto, HttpSession session) {
        User user = userRepository.findByUsernameAndPassword(loginDto.getUsername(), loginDto.getPassword())
                .orElseThrow(() -> new InvalidLoginException());
        // "Log" the user in by setting the session attribute
        session.setAttribute("user", user.getId());
        return modelMapper.map(user, UserDto.class);
    }

    public UserDto registerUser(LoginRegisterDto registerDto) {
        // If the username already exists, throw an exception since we cannot have duplicates
        if (userRepository.existsByUsername(registerDto.getUsername())) {
            throw new UsernameTakenException();
        }

        // Create and save new user to repository
        User user = new User(registerDto.getUsername(), registerDto.getPassword());
        userRepository.save(user);

        // Map and return back the created user
        return modelMapper.map(user, UserDto.class);
    }
}
