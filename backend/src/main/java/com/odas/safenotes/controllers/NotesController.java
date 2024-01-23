package com.odas.safenotes.controllers;

import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notes")
public class NotesController {

    @GetMapping
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Hello World!");
    }

    @GetMapping("/{userId}")
    public ResponseEntity<String> test2(@PathVariable String userId) {
        return ResponseEntity.ok("Hello World!");
    }

    @PostMapping("/{userId}")
    public ResponseEntity<Void> test3() {
        return ResponseEntity.noContent().build();
    }
}
