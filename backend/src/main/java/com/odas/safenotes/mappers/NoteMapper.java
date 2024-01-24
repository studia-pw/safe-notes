package com.odas.safenotes.mappers;

import com.odas.safenotes.domain.Note;
import com.odas.safenotes.domain.User;
import com.odas.safenotes.dto.note.CreateNoteRequest;
import com.odas.safenotes.dto.note.DecodedNoteResource;
import com.odas.safenotes.dto.note.NoteResource;
import org.springframework.stereotype.Component;

@Component
public class NoteMapper {

    public Note fromCreateNoteRequest(CreateNoteRequest createNoteRequest, User user) {
        return Note.builder()
                .id(null)
                .author(user)
                .title(createNoteRequest.title())
                .content(createNoteRequest.content())
                .hashedPassword(createNoteRequest.password())
                .isPublic(createNoteRequest.isPublic())
                .build();
    }

    public NoteResource fromNoteToNoteResource(Note note) {
        return NoteResource.builder()
                .id(note.getId())
                .title(note.getTitle())
                .isEncrypted(note.getHashedPassword() != null)
                .content(note.getHashedPassword() != null ? "" : note.getContent())
                .build();
    }

    public DecodedNoteResource fromNoteToDecodedNoteResource(Note decodedNote) {
        return DecodedNoteResource.builder()
                .id(decodedNote.getId())
                .title(decodedNote.getTitle())
                .isEncrypted(decodedNote.getHashedPassword() != null)
                .content(decodedNote.getContent())
                .build();
    }
}
