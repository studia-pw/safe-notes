package com.odas.safenotes.services;

import com.odas.safenotes.domain.Note;
import com.odas.safenotes.domain.User;
import com.odas.safenotes.dto.note.CreateNoteRequest;
import com.odas.safenotes.mappers.NoteMapper;
import com.odas.safenotes.repositories.NoteRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NoteService {

    private final NoteRepository noteRepository;
    private final NoteMapper noteMapper;
    private final PasswordEncoder encoder;

    @Transactional
    public void createNote(CreateNoteRequest createNoteRequest) {
        User user = null;
        final var request = encodePasswordIfNeeded(createNoteRequest);
        Note note = noteMapper.fromCreateNoteRequest(request, user);
        noteRepository.save(note);
    }

    private CreateNoteRequest encodePasswordIfNeeded(CreateNoteRequest createNoteRequest) {
        if (createNoteRequest.isPublic() || !createNoteRequest.isEncrypted()) {
            return CreateNoteRequest.builder()
                    .title(createNoteRequest.title())
                    .content(createNoteRequest.content())
                    .password(null)
                    .isPublic(createNoteRequest.isPublic())
                    .isEncrypted(createNoteRequest.isEncrypted())
                    .build();
        }

        String hashedPassword = encoder.encode(createNoteRequest.password());

        return CreateNoteRequest.builder()
                .title(createNoteRequest.title())
                .content(createNoteRequest.content())
                .password(hashedPassword)
                .isPublic(false)
                .isEncrypted(true)
                .build();
    }
}
