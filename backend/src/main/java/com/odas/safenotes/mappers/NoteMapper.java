package com.odas.safenotes.mappers;

import com.odas.safenotes.domain.Note;
import com.odas.safenotes.domain.User;
import com.odas.safenotes.dto.note.CreateNoteRequest;
import org.springframework.stereotype.Component;

@Component
public class NoteMapper {

    public Note fromCreateNoteRequest(CreateNoteRequest createNoteRequest, User user) {
        return Note.builder()
                .id(null)
                .title(createNoteRequest.title())
                .content(createNoteRequest.content())
                .hashedPassword(createNoteRequest.password())
                .isPublic(createNoteRequest.isPublic())
                .build();
    }
}
