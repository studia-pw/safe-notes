package com.odas.safenotes.services;

import com.odas.safenotes.domain.Note;
import com.odas.safenotes.domain.User;
import com.odas.safenotes.dto.note.CreateNoteRequest;
import com.odas.safenotes.dto.note.DecodeNoteRequest;
import com.odas.safenotes.dto.note.DecodedNoteResource;
import com.odas.safenotes.dto.note.NoteResource;
import com.odas.safenotes.mappers.NoteMapper;
import com.odas.safenotes.repositories.NoteRepository;
import com.odas.safenotes.util.NoteEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NoteService {

    private final NoteRepository noteRepository;
    private final NoteMapper noteMapper;
    private final PasswordEncoder encoder;
    private final NoteEncoder noteEncoder;

    @Transactional
    public void createNote(CreateNoteRequest createNoteRequest) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth instanceof AnonymousAuthenticationToken) {
            return;
        }

        User user = (User) auth.getPrincipal();
        var request = doCreateNoteRequestCleanup(createNoteRequest);
        Note note = noteMapper.fromCreateNoteRequest(request, user);

        encodePasswordIfNeeded(note);
        encodeContentIfNeeded(note, createNoteRequest.password());
        noteRepository.save(note);
    }

    @Transactional(readOnly = true)
    public List<NoteResource> getUserNotes(Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth instanceof AnonymousAuthenticationToken) {
            System.out.println("User is not authenticated");
            return null;
        }

        User user = (User) auth.getPrincipal();

        if (!user.getId().equals(id)) {
            System.out.println("User id does not match");
            return null;
        }

        final var notes = noteRepository.findAllByAuthor(user);
        return notes.stream().map(noteMapper::fromNoteToNoteResource).collect(Collectors.toList());
    }

    @Transactional
    public DecodedNoteResource getDecryptedUserNoteById(Long noteId, DecodeNoteRequest decodeNoteRequest) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth instanceof AnonymousAuthenticationToken) {
            System.out.println("User is not authenticated");
            return null;
        }

        User user = (User) auth.getPrincipal();

        final var note = noteRepository.findById(noteId).orElseThrow();

        if (!note.getAuthor().getId().equals(user.getId())) {
            System.out.println("User id does not match");
            return null;
        }

        final var passwordHash = note.getHashedPassword();
        if (passwordHash == null || !encoder.matches(decodeNoteRequest.password(), passwordHash)) {
            throw new AccessDeniedException("Wrong password");
        }

        Note decodedNote = decodeNote(note, decodeNoteRequest.password());
        return noteMapper.fromNoteToDecodedNoteResource(decodedNote);
    }

    @Transactional(readOnly = true)
    public List<NoteResource> getPublicNotes() {
        final var notes = noteRepository.findAllByIsPublicTrue();
        return notes.stream().map(noteMapper::fromNoteToNoteResource).collect(Collectors.toList());
    }

    private CreateNoteRequest doCreateNoteRequestCleanup(CreateNoteRequest createNoteRequest) {
        if (createNoteRequest.isPublic() || !createNoteRequest.isEncrypted()) {
            return CreateNoteRequest.builder()
                    .title(createNoteRequest.title())
                    .content(createNoteRequest.content())
                    .password(null)
                    .isPublic(createNoteRequest.isPublic())
                    .isEncrypted(false)
                    .build();
        }

        return CreateNoteRequest.builder()
                .title(createNoteRequest.title())
                .content(createNoteRequest.content())
                .password(createNoteRequest.password())
                .isPublic(false)
                .isEncrypted(true)
                .build();
    }

    private void encodePasswordIfNeeded(Note note) {
        if (note.getHashedPassword() == null) {
            return;
        }

        note.setHashedPassword(encoder.encode(note.getHashedPassword()));
    }

    private void encodeContentIfNeeded(Note note, String password) {
        if (note.getIsPublic() || note.getHashedPassword() == null) {
            return;
        }

        String encryptedContent = null;
        IvParameterSpec iv = null;
        try {
            String algorithm = "AES/CBC/PKCS5Padding";
            SecretKey key = noteEncoder.generateKeyFromPassword(password, note.getHashedPassword());
            iv = noteEncoder.generateIv();
            encryptedContent = noteEncoder.encode(algorithm, note.getContent(), key, iv);
        } catch (Exception e) {
            throw new RuntimeException("Error while encrypting note content");
        }

        note.setContent(encryptedContent);
        note.setIv(iv.getIV());
    }

    private Note decodeNote(Note note, String rawPassword) {
        if (note.getHashedPassword() == null) {
            throw new NullPointerException("Note is not encrypted");
        }

        String decodedContent = null;
        try {
            String encodedContent = note.getContent();
            SecretKey key = noteEncoder.generateKeyFromPassword(rawPassword, note.getHashedPassword());
            IvParameterSpec iv = new IvParameterSpec(note.getIv());
            String algorithm = "AES/CBC/PKCS5Padding";
            decodedContent = noteEncoder.decode(algorithm, encodedContent, key, iv);
        } catch (Exception e) {
            throw new RuntimeException("Error while decrypting note content");
        }

        return Note.builder()
                .id(note.getId())
                .title(note.getTitle())
                .content(decodedContent)
                .hashedPassword(note.getHashedPassword())
                .isPublic(note.getIsPublic())
                .iv(note.getIv())
                .author(note.getAuthor())
                .build();
    }
}
