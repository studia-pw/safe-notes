package com.odas.safenotes.dto.note;


import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.extern.jackson.Jacksonized;

@Jacksonized
@Builder
public record CreateNoteRequest(
        String title,
        @Size(min = 1, max = 10000)
        String content,
        String password,
        Boolean isPublic,
        Boolean isEncrypted
) {
}
