package com.odas.safenotes.dto.user;

import lombok.Builder;

@Builder
public record UserResource(
        Long id,
        String email
) {
}
