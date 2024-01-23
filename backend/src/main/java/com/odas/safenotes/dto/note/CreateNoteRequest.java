package com.odas.safenotes.dto.note;


import lombok.Builder;

@Builder
public record CreateNoteRequest (
        String title,
        String content,
        String password,
        Boolean isPublic,
        Boolean isEncrypted
) {}
