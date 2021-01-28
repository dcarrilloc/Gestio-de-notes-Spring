package com.esliceu.services;

import com.esliceu.entities.Note;
import com.esliceu.entities.User;
import com.esliceu.repos.NoteRepo;
import com.esliceu.repos.UserRepo;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.commonmark.renderer.text.TextContentRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class NoteServiceImpl implements NoteService {
    @Autowired
    NoteRepo noteRepo;

    @Autowired
    UserRepo userRepo;


    public List<Note> getAllNotes() {
        return noteRepo.findAll();
    }

    public Note getNoteById(Long noteid) {
        return noteRepo.findById(noteid).get();
    }

    public void saveNote(Long noteid, String title, String body, Long userid) {
        Note n = new Note();
        n.setNoteid(noteid);
        n.setTitle(title);
        n.setBody(body);
        n.setCreationDate(LocalDateTime.now());
        n.setLastModDate(LocalDateTime.now());
        n.setOwner(userRepo.findById(userid).get());
        noteRepo.save(n);
    }

    public void deleteNote(Long userid, Long noteid) {
        //User user = userRepo.findById(userid).get();
        Note note = noteRepo.findById(noteid).get();
        if(checkNoteOwnership(userid, noteid)) {
            // If user owns the note he can delete the note from the ddbb.
            noteRepo.delete(note);
        } else {
            // If user not owns the note he cannot delete it from the owner but only for himself.
            System.out.println("no eres owner");
        }
    }

    public List<Note> getFeedNotesByUser(Long userid){
        User owner = userRepo.findById(userid).get();
        return noteRepo.findAllByOwner(owner);
    }

    public boolean checkNoteOwnership(Long userid, Long noteid) {
        Optional<Note> noteOptional = noteRepo.findById(noteid);
        if(noteOptional.isPresent()) {
            Note note = noteOptional.get();
            return userid.equals(note.getOwner().getUserid());
        }
        return false;
    }

    public Note getParsedNote(Long noteid, boolean toHTML) {
        Note note = noteRepo.findById(noteid).get();
        Parser parser = Parser.builder().build();
        Node document = parser.parse(note.getBody());

        Note renderedNote = new Note();
        renderedNote.setNoteid(noteid);
        renderedNote.setTitle(note.getTitle());
        renderedNote.setCreationDate(note.getCreationDate());
        renderedNote.setLastModDate(note.getLastModDate());
        renderedNote.setOwner(note.getOwner());

        if(toHTML) {
            // If note is going to be rendered as HTML.
            HtmlRenderer renderer =HtmlRenderer.builder().escapeHtml(true).sanitizeUrls(true).build();
            String parsedBody = renderer.render(document);
            renderedNote.setBody(parsedBody);
        } else{
            // If note is going to be rendered as plain text.
            TextContentRenderer renderer = TextContentRenderer.builder().build();
            String parsedBody = renderer.render(document);
            renderedNote.setBody(parsedBody);
        }

        return renderedNote;
    }



}
