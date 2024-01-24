package com.odas.safenotes.controllers;

import com.odas.safenotes.dto.note.CreateNoteRequest;
import com.odas.safenotes.dto.note.DecodeNoteRequest;
import com.odas.safenotes.dto.note.DecodedNoteResource;
import com.odas.safenotes.dto.note.NoteResource;
import com.odas.safenotes.services.NoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notes")
public class NotesController {

    private final NoteService noteService;

    @GetMapping
    public ResponseEntity<List<NoteResource>> getPublicNotes() {
        final var notes = noteService.getPublicNotes();
        return ResponseEntity.ok(notes);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<NoteResource>> getPrivateNotes(@PathVariable Long userId) {
        final var notes = noteService.getUserNotes(userId);
        return ResponseEntity.ok(notes);
    }

    @PostMapping("/private/{noteId}")
    public ResponseEntity<DecodedNoteResource> getPrivateNote(
            @PathVariable Long noteId,
            @RequestBody DecodeNoteRequest decodeNoteRequest
    ) {
        final var note = noteService.getDecryptedUserNoteById(noteId, decodeNoteRequest);
        return ResponseEntity.ok(note);
    }

    @PostMapping()
    public ResponseEntity<Void> createNote(@RequestBody CreateNoteRequest createNoteRequest) {
        noteService.createNote(createNoteRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
