package com.example.showcased.service;

import com.example.showcased.dto.LoginDto;
import com.example.showcased.dto.RegisterDto;
import com.example.showcased.entity.User;
import com.example.showcased.exception.EmailTakenException;
import com.example.showcased.exception.InvalidLoginException;
import com.example.showcased.exception.UsernameTakenException;
import com.example.showcased.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Function that verifies that the login credentials are valid
    public void loginUser(LoginDto loginDto, HttpSession session) {
        User user = userRepository.findByEmail(loginDto.getEmail())
                .orElseThrow(() -> new InvalidLoginException());

        // Check if the provided password matches the stored encrypted password
        if (!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
            throw new InvalidLoginException();
        }

        // "Log" the user in by setting the session attribute
        session.setAttribute("user", user.getId());
    }

    public void registerUser(RegisterDto registerDto) {
        // If the username already exists, throw an exception since we cannot have duplicates
        if (userRepository.existsByUsername(registerDto.getUsername())) {
            throw new UsernameTakenException("Username is already taken");
        }

        // If the email is already associated with an account, throw an exception
        if (userRepository.existsByEmail(registerDto.getEmail())) {
            throw new EmailTakenException("Email is already associated with an account");
        }

        // Create and save new user to repository
        // Use the encryptor to encrypt the password before storing
        String encodedPassword = passwordEncoder.encode(registerDto.getPassword());
        User user = new User(registerDto.getEmail(), registerDto.getUsername(), encodedPassword);
        userRepository.save(user);
    }
}
