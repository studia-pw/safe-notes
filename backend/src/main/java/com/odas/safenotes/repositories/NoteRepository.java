package com.odas.safenotes.repositories;

import com.odas.safenotes.domain.Note;
import com.odas.safenotes.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoteRepository extends JpaRepository<Note, Long> {

    List<Note> findAllByIsPublicTrue();

    List<Note> findAllByAuthor(User user);
}
