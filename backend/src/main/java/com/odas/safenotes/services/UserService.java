package com.odas.safenotes.services;

import com.odas.safenotes.dto.user.RegisterUserRequest;
import com.odas.safenotes.mappers.UserMapper;
import com.odas.safenotes.repositories.UserRepository;
import com.odas.safenotes.util.PasswordStrength;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordStrength passwordStrength;

    public void register(RegisterUserRequest registerUserRequest) {
        if (userRepository.existsByEmailIgnoreCase(registerUserRequest.email())) {
            throw new IllegalArgumentException("User with this email already exists");
        }

        if (!registerUserRequest.password().equals(registerUserRequest.passwordConfirmation())) {
            throw new IllegalArgumentException("Passwords do not match");
        }

        if (!passwordStrength.passwordIsStrongEnough(registerUserRequest.password())) {
            System.out.println("Password is not strong enough");
            throw new IllegalArgumentException("Password is not strong enough");
        }

        final var user = userMapper.fromRegisterUserRequest(registerUserRequest);
        userRepository.save(user);
    }


}
