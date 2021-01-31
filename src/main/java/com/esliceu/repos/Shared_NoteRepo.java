package com.esliceu.repos;

import com.esliceu.entities.Note;
import com.esliceu.entities.Shared_Note;
import com.esliceu.entities.Shared_NoteCK;
import com.esliceu.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface Shared_NoteRepo extends JpaRepository<Shared_Note, Shared_NoteCK> {
    List<Shared_Note> findAllByNote(Note note);
    Optional<Shared_Note> findByNoteAndUser(Note note, User user);
}
