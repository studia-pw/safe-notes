package com.odas.safenotes.controllers;

import com.odas.safenotes.dto.note.CreateNoteRequest;
import com.odas.safenotes.services.NoteService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notes")
public class NotesController {

    private final NoteService noteService;

    @GetMapping
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Hello World!");
    }

    @GetMapping("/{userId}")
    public ResponseEntity<String> test2(@PathVariable String userId) {
        return ResponseEntity.ok("Hello World!");
    }

    @PostMapping()
    public ResponseEntity<Void> createNote(@RequestBody CreateNoteRequest createNoteRequest) {
        noteService.createNote(createNoteRequest);
        return ResponseEntity.noContent().build();
    }
}
