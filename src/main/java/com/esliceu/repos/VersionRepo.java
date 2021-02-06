package com.esliceu.repos;

import com.esliceu.entities.Note;
import com.esliceu.entities.Version;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

public interface VersionRepo extends JpaRepository<Version, Long> {
    Set<Version> findAllByNoteOrderByCreationDateDesc(Note note);
    Optional<Version> findByVersionid(Long versionid);
}
