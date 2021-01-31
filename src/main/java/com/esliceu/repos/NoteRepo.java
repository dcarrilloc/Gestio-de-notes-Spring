package com.esliceu.repos;

import com.esliceu.entities.Note;
import com.esliceu.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NoteRepo extends JpaRepository<Note, Long> {
    List<Note> findAllByOwner(User owner);

    @Query(value = "from Note n where n.noteid in (select n1.noteid from Note n1 where n1.owner.userid = :userid) or n.noteid in (select n2.note.noteid from Shared_note n2 where n2.user.userid = :userid) ")
    List<Note> getFeedNotesByUser(@Param("userid") Long userid);
}
