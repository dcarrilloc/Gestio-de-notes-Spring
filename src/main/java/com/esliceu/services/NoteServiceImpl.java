package com.esliceu.services;

import com.esliceu.entities.*;
import com.esliceu.repos.NoteRepo;
import com.esliceu.repos.Shared_NoteRepo;
import com.esliceu.repos.UserRepo;
import com.esliceu.repos.VersionRepo;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.commonmark.renderer.text.TextContentRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class NoteServiceImpl implements NoteService {
    @Autowired
    NoteRepo noteRepo;

    @Autowired
    UserRepo userRepo;

    @Autowired
    Shared_NoteRepo shared_noteRepo;

    @Autowired
    VersionRepo versionRepo;


    public Note getNoteById(Long noteid) {
        Optional<Note> optionalNote = noteRepo.findById(noteid);
        if(optionalNote.isPresent()) {
            return optionalNote.get();
        }
        return null;
    }

    public void saveNote(Long noteid, String title, String body, Long userid) {
        Note n = new Note();
        if(noteid != null) {
            // Editant nota
            Optional<Note> optionalNote = noteRepo.findById(noteid);
            n.setNoteid(optionalNote.get().getNoteid());
            n.setCreationDate(optionalNote.get().getCreationDate());
            Optional<User> optionalOwner = userRepo.findById(optionalNote.get().getOwner().getUserid());
            if(optionalOwner.isPresent()) {
                n.setOwner(optionalNote.get().getOwner());
            } else {
                Optional<User> newOwner = userRepo.findById(userid);
                newOwner.ifPresent(n::setOwner);
            }
        } else {
            // Creant nota
            n.setNoteid(null);
            n.setCreationDate(LocalDateTime.now());
            Optional<User> owner = userRepo.findById(userid);
            owner.ifPresent(n::setOwner);
        }

        n.setTitle(title);
        n.setBody(body);
        n.setLastModDate(LocalDateTime.now());

        Version v = new Version();
        v.setTitle(title);
        v.setBody(body);
        v.setCreationDate(LocalDateTime.now());
        v.setNote(n);
        Optional<User> editor = userRepo.findById(userid);
        editor.ifPresent(v::setUser);

        noteRepo.save(n);
        versionRepo.save(v);
    }

    public void deleteNote(Long userid, Long[] noteid) {
        List<Long> noteList = Arrays.asList(noteid);
        noteList.forEach(note -> {
            Optional<Note> optionalNote = noteRepo.findById(note);
            Optional<User> optionalUser = userRepo.findById(userid);
            if (optionalNote.isPresent() && optionalUser.isPresent()) {
                Note noteToDelete = optionalNote.get();
                User user = optionalUser.get();
                if (checkNoteOwnership(user.getUserid(), noteToDelete.getNoteid())) {
                    // If user owns the note he can delete the note from the ddbb.
                    noteRepo.delete(noteToDelete);
                } else {
                    // If user not owns the note he cannot delete it from the owner but only for himself.
                    if(checkNoteIsSharedByUserId(user.getUserid(), noteToDelete.getNoteid())) {
                        Optional<Shared_Note> shared_note = shared_noteRepo.findByNoteAndUser(noteToDelete, user);
                        shared_note.ifPresent(sharedNote -> shared_noteRepo.delete(sharedNote));
                    }
                }
            }
        });
    }

    public List<Note> getFeedNotesByUser(Long userid, LocalDateTime dateFrom, LocalDateTime datoTo, String searchValue) {
        Optional<User> optionalUser = userRepo.findById(userid);
        if(optionalUser.isPresent()) {
            User user = optionalUser.get();
            return noteRepo.getFeedNotesByUser(user.getUserid(), dateFrom, datoTo, searchValue);
        }
        return new ArrayList<>();
    }

    public List<Note> getPagedNotes(Long userid, LocalDateTime dateFrom, LocalDateTime datoTo, String searchValue, Integer page, String column, String order) {
        Pageable pageable;
        if(order.equals("asc")) {
            pageable = PageRequest.of(page, 10, Sort.by(column).ascending());
        } else {
            pageable = PageRequest.of(page, 10, Sort.by(column).descending());
        }

        Optional<User> optionalUser = userRepo.findById(userid);
        if(optionalUser.isPresent()) {
            User user = optionalUser.get();
            return noteRepo.getPagedNotes(user.getUserid(), dateFrom, datoTo, searchValue, pageable);
        }
        return new ArrayList<>();
    }

    public boolean checkNoteOwnership(Long userid, Long noteid) {
        Optional<Note> noteOptional = noteRepo.findById(noteid);
        if (noteOptional.isPresent()) {
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

        if (toHTML) {
            // If note is going to be rendered as HTML.
            HtmlRenderer renderer = HtmlRenderer.builder().escapeHtml(true).sanitizeUrls(true).build();
            String parsedBody = renderer.render(document);
            renderedNote.setBody(parsedBody);
        } else {
            // If note is going to be rendered as plain text.
            TextContentRenderer renderer = TextContentRenderer.builder().build();
            String parsedBody = renderer.render(document);
            renderedNote.setBody(parsedBody);
        }

        return renderedNote;
    }

    public void shareNote(String username, Long noteid, Long userid, String permissionMode) {
        Optional<Note> optionalNote = noteRepo.findById(noteid);
        Optional<User> optionalUser = userRepo.findById(userid);
        User user = userRepo.findByUsername(username);

        if (optionalNote.isPresent() && optionalUser.isPresent() && user != null) {
            Note note = optionalNote.get();
            User user1 = optionalUser.get();
            if (checkEditorPermission(note, user1) || checkNoteOwnership(user1.getUserid(), note.getNoteid())) {
                Shared_Note shared_note = new Shared_Note();
                shared_note.setId(new Shared_NoteCK(note.getNoteid(), user.getUserid()));
                shared_note.setNote(note);
                shared_note.setUser(user);
                shared_note.setSharedDate(LocalDateTime.now());
                shared_note.setPermissionMode(permissionMode);

                System.out.println(shared_note.toString());

                shared_noteRepo.save(shared_note);
            }
        }
    }

    public String checkPermission(Long userid, Long noteid) {
        Optional<Note> optionalNote = noteRepo.findById(noteid);
        Optional<User> optionalUser = userRepo.findById(userid);

        if(optionalUser.isPresent() && optionalNote.isPresent()) {
            Note note = optionalNote.get();
            User user = optionalUser.get();
            Optional<Shared_Note> optionalShared_note = shared_noteRepo.findByNoteAndUser(note, user);
            if(optionalShared_note.isPresent()) {
                return optionalShared_note.get().getPermissionMode();
            }
        }
        return "";
    }

    public boolean checkEditorPermission(Note note, User user) {
        Optional<Shared_Note> optionalShared_note = shared_noteRepo.findByNoteAndUser(note, user);
        return optionalShared_note.map(shared_note -> shared_note.getPermissionMode().equals("EDITOR")).orElse(false);
    }

    public List<User> getSharedUsersFromNote(Long sessionUserId, Long noteid) {
        // First check user is owner. If not, user cannot see the users that the note has been shared.
        if (checkNoteOwnership(sessionUserId, noteid)) {
            // User is owner
            Optional<Note> optionalNote = noteRepo.findById(noteid);
            if(optionalNote.isPresent()) {
                Note note = optionalNote.get();
                List<Shared_Note> shared_notes = shared_noteRepo.findAllByNote(note);
                List<User> usersList = new ArrayList<>();
                for (Shared_Note sn : shared_notes) {
                    Optional<User> optionalUser = userRepo.findById(sn.getUser().getUserid());
                    optionalUser.ifPresent(usersList::add);
                }

                return usersList;
            }
        }
        return new ArrayList<>();
    }

    public boolean checkNoteIsSharedByUserId(Long userid, Long noteid) {
        Optional<User> optionalUser = userRepo.findById(userid);
        Optional<Note> optionalNote = noteRepo.findById(noteid);
        if(optionalUser.isPresent() && optionalNote.isPresent()) {
            Optional<Shared_Note> optionalShared_note = shared_noteRepo.findByNoteAndUser(optionalNote.get(), optionalUser.get());
            return optionalShared_note.isPresent();
        }
        return false;
    }

    public void deleteUserFromSharedNote(Long noteid, Long ownerid, Long userid) {
        if(checkNoteOwnership(ownerid, noteid)) {
            Optional<Note> optionalNote = noteRepo.findById(noteid);
            Optional<User> optionalUser = userRepo.findById(userid);
            Optional<User> optionalOwner = userRepo.findById(ownerid);

            if(optionalNote.isPresent() && optionalUser.isPresent() && optionalOwner.isPresent()) {
                Optional<Shared_Note> shared_note = shared_noteRepo.findByNoteAndUser(optionalNote.get(), optionalUser.get());
                shared_note.ifPresent(sharedNote -> shared_noteRepo.delete(sharedNote));
            }
        }
    }
}









