package com.example.showcased.service;

import com.example.showcased.dto.LoginRegisterDto;
import com.example.showcased.dto.UserDto;
import com.example.showcased.entity.User;
import com.example.showcased.exception.InvalidLoginException;
import com.example.showcased.exception.UsernameTakenException;
import com.example.showcased.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, ModelMapper modelMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
    }

    // Function that verifies that the login credentials are valid
    public UserDto loginUser(LoginRegisterDto loginDto, HttpSession session) {
        User user = userRepository.findByUsername(loginDto.getUsername())
                .orElseThrow(() -> new InvalidLoginException());

        // Check if the provided password matches the stored encrypted password
        if (!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
            throw new InvalidLoginException();
        }

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
        // Use the encryptor to encrypt the password before storing
        String encodedPassword = passwordEncoder.encode(registerDto.getPassword());
        User user = new User(registerDto.getUsername(), encodedPassword);
        userRepository.save(user);

        // Map and return back the created user
        return modelMapper.map(user, UserDto.class);
    }
}
