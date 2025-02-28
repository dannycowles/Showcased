package com.example.showcased.service;

import com.example.showcased.dto.UserDto;
import com.example.showcased.entity.User;
import com.example.showcased.exception.UserNotFoundException;
import com.example.showcased.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public LoginService(UserRepository userRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    // Function to retrieve user by id
    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        return modelMapper.map(user, UserDto.class);
    }
}
