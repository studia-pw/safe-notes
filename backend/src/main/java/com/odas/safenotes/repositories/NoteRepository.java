package com.odas.safenotes.repositories;

import com.odas.safenotes.domain.Note;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoteRepository extends JpaRepository<Note, Long> {
}
