package com.esliceu.services;

import com.esliceu.entities.Note;
import com.esliceu.entities.Version;
import com.esliceu.repos.NoteRepo;
import com.esliceu.repos.VersionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class VersionServiceImpl implements VersionService {

    @Autowired
    VersionRepo versionRepo;

    @Autowired
    NoteRepo noteRepo;


    public Set<Version> getVersionFromNote(Long noteid) {
        Optional<Note> optionalNote = noteRepo.findById(noteid);
        if(optionalNote.isPresent()) {
            Note note = optionalNote.get();
            return versionRepo.findAllByNoteOrderByCreationDateDesc(note);
        }
        return null;
    }
}
