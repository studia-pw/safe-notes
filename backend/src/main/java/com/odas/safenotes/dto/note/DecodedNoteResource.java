package com.odas.safenotes.dto.note;

import lombok.Builder;
import lombok.extern.jackson.Jacksonized;

@Jacksonized
@Builder
public record DecodedNoteResource(
        Long id,
        String title,
        String content,
        Boolean isEncrypted
) {
}
