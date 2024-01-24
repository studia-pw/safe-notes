package com.odas.safenotes.dto.note;


import lombok.Builder;
import lombok.extern.jackson.Jacksonized;

@Jacksonized
@Builder
public record CreateNoteRequest(
        String title,
        String content,
        String password,
        Boolean isPublic,
        Boolean isEncrypted
) {
}
