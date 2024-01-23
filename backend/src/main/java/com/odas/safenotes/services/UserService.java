package com.odas.safenotes.services;

import com.odas.safenotes.dto.user.RegisterUserRequest;
import com.odas.safenotes.mappers.UserMapper;
import com.odas.safenotes.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public void register(RegisterUserRequest registerUserRequest) {
        if (userRepository.existsByEmailIgnoreCase(registerUserRequest.email())) {
            throw new IllegalArgumentException("User with email " + registerUserRequest.email() + " already exists");
        }

        if (!registerUserRequest.password().equals(registerUserRequest.passwordConfirmation())) {
            throw new IllegalArgumentException("Passwords do not match");
        }

        final var user = userMapper.fromRegisterUserRequest(registerUserRequest);
        userRepository.save(user);
    }


}
