package com.odas.safenotes.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record RegisterUserRequest(
        @Email
        String email,

        @Size(min = 8)
        String password,

        @Size(min = 8)
        String passwordConfirmation
) {
}
