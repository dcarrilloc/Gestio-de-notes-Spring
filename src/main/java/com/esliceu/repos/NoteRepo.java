package com.esliceu.repos;

import com.esliceu.entities.Note;
import com.esliceu.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoteRepo extends JpaRepository<Note, Long> {
    public List<Note> findAllByOwner(User owner);
}
