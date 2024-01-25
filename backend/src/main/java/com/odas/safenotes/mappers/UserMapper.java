package com.odas.safenotes.mappers;

import com.odas.safenotes.domain.User;
import com.odas.safenotes.dto.user.RegisterUserRequest;
import com.odas.safenotes.dto.user.UserResource;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {
    private final PasswordEncoder encoder;

    public User fromRegisterUserRequest(RegisterUserRequest registerUserRequest) {
        return User.builder()
                .id(null)
                .email(registerUserRequest.email())
                .password(encoder.encode(registerUserRequest.password()))
                .build();
    }

    public UserResource fromUser(User user) {
        return UserResource.builder()
                .id(user.getId())
                .email(user.getEmail())
                .build();
    }
}
