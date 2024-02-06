package com.odas.safenotes.dto.user;

import jakarta.validation.constraints.Email;

public record TotpQrRequest(
        @Email
        String email
) {
}
