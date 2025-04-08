package com.example.showcased.service;

import com.example.showcased.dto.LoginDto;
import com.example.showcased.dto.LoginStatusDto;
import com.example.showcased.dto.RegisterDto;
import com.example.showcased.dto.UsernameCheckDto;
import com.example.showcased.entity.OtpRequest;
import com.example.showcased.entity.User;
import com.example.showcased.exception.EmailTakenException;
import com.example.showcased.exception.InvalidLoginException;
import com.example.showcased.exception.UsernameTakenException;
import com.example.showcased.repository.OtpRequestRepository;
import com.example.showcased.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final OtpRequestRepository otpRequestRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       OtpRequestRepository otpRequestRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.otpRequestRepository = otpRequestRepository;
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

    public LoginStatusDto loginStatus(HttpSession session) {
        Long id = (Long) session.getAttribute("user");
        LoginStatusDto loginStatusDto = new LoginStatusDto();
        loginStatusDto.setLoggedIn(id != null);
        return loginStatusDto;
    }

    public void requestOTP(String email) {
        // Find the user associated with the specified email
        // If the email is not associated return and provide no error message
        // This is for security purposes
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            return;
        }

        // Check if the user already has an entry in the OTP table, if we so delete it
        // This is useful for case when user requests a new OTP whether they didn't receive first
        // one or it expired
        Optional<OtpRequest> otpRequest = otpRequestRepository.findById(user.get().getId());
        otpRequest.ifPresent(otpRequestRepository::delete);

        // Generate a random 6-digit number and pad it with 0s if necessary
        SecureRandom random = new SecureRandom();
        int otp = random.nextInt(1000000);
        String otpPadded = String.format("%06d", otp);

        // TODO: send email to user's email with otpPadded

        // Save the new entry to the database, expiration time is 5 minutes after creation
        OtpRequest newOtpRequest = new OtpRequest();
        newOtpRequest.setUserId(user.get().getId());
        newOtpRequest.setOtp(otpPadded);
        newOtpRequest.setExpiresAt(new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(5)));
        otpRequestRepository.save(newOtpRequest);
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

    public UsernameCheckDto checkUsernameAvailability(String username) {
        UsernameCheckDto usernameCheckDto = new UsernameCheckDto();
        usernameCheckDto.setTaken(userRepository.existsByUsername(username));
        return usernameCheckDto;
    }
}
