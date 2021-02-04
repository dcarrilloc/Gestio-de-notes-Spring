package com.esliceu.repos;

import com.esliceu.entities.Note;
import com.esliceu.entities.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface NoteRepo extends JpaRepository<Note, Long> {
    List<Note> findAllByOwner(User owner);
    @Query(value = "from Note n where n.noteid in (select n1.noteid from Note n1 where n1.owner.userid = :userid and (n1.creationDate between :dateFrom and :dateTo ) and (n1.title like %:searchValue% or n1.body like %:searchValue%)) or n.noteid in (select n2.note.noteid from Shared_note n2 where n2.user.userid = :userid and (n2.note.creationDate between :dateFrom and :dateTo) and (n2.note.title like %:searchValue% or n2.note.body like %:searchValue%))")
    List<Note> getFeedNotesByUser(@Param("userid") Long userid, @Param("dateFrom") LocalDateTime dateFrom, @Param("dateTo") LocalDateTime dateTo, @Param("searchValue") String searchValue);

    @Query(value = "from Note n where n.noteid in (select n1.noteid from Note n1 where n1.owner.userid = :userid and (n1.creationDate between :dateFrom and :dateTo ) and (n1.title like %:searchValue% or n1.body like %:searchValue%)) or n.noteid in (select n2.note.noteid from Shared_note n2 where n2.user.userid = :userid and (n2.note.creationDate between :dateFrom and :dateTo) and (n2.note.title like %:searchValue% or n2.note.body like %:searchValue%))")
    List<Note> getPagedNotes(@Param("userid") Long userid, @Param("dateFrom") LocalDateTime dateFrom, @Param("dateTo") LocalDateTime dateTo, @Param("searchValue") String searchValue, Pageable pageable);

}
