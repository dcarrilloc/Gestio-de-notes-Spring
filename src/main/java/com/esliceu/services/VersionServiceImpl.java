package com.esliceu.services;

import com.esliceu.entities.Note;
import com.esliceu.entities.User;
import com.esliceu.entities.Version;
import com.esliceu.repos.NoteRepo;
import com.esliceu.repos.UserRepo;
import com.esliceu.repos.VersionRepo;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.commonmark.renderer.text.TextContentRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class VersionServiceImpl implements VersionService {

    @Autowired
    VersionRepo versionRepo;

    @Autowired
    UserRepo userRepo;

    @Autowired
    NoteRepo noteRepo;

    @Autowired
    NoteServiceImpl noteService;

    public Version getVersionFromId(Long versionid) {
        Optional<Version> optionalVersion = versionRepo.findById(versionid);
        return optionalVersion.orElse(null);
    }

    public Set<Version> getVersionFromNote(Long noteid) {
        Optional<Note> optionalNote = noteRepo.findById(noteid);
        if(optionalNote.isPresent()) {
            Note note = optionalNote.get();
            return versionRepo.findAllByNoteOrderByCreationDateDesc(note);
        }
        return null;
    }

    public Note getNoteFromVersionId(Long versionid) {
        Optional<Version> optionalVersion = versionRepo.findByVersionid(versionid);
        System.out.println(optionalVersion.toString());
        return optionalVersion.map(Version::getNote).orElse(null);
    }

    public Version getParsedBody(Long versionid, boolean toHTML) {
        Optional<Version> optionalVersion = versionRepo.findByVersionid(versionid);
        if(optionalVersion.isPresent()) {
            Version version = optionalVersion.get();
            Parser parser = Parser.builder().build();
            Node document = parser.parse(version.getBody());

            Version renderedVersion = new Version();
            renderedVersion.setVersionid(versionid);
            renderedVersion.setTitle(version.getTitle());
            renderedVersion.setCreationDate(version.getCreationDate());
            renderedVersion.setEditor(version.getEditor());

            if (toHTML) {
                // If version is going to be rendered as HTML.
                HtmlRenderer renderer = HtmlRenderer.builder().escapeHtml(true).sanitizeUrls(true).build();
                String parsedBody = renderer.render(document);
                renderedVersion.setBody(parsedBody);
            } else {
                // If version is going to be rendered as plain text.
                TextContentRenderer renderer = TextContentRenderer.builder().build();
                String parsedBody = renderer.render(document);
                renderedVersion.setBody(parsedBody);
            }
            return renderedVersion;
        }
        return null;
    }

    public void makeCopy(Long versionid, Long userid) throws Exception {
        Optional<Version> optionalVersion = versionRepo.findById(versionid);
        Optional<User> optionalUser = userRepo.findById(userid);
        if(optionalVersion.isPresent() && optionalUser.isPresent()) {
            User user = optionalUser.get();
            Version version = optionalVersion.get();
            noteService.saveNote(null, version.getTitle(), version.getBody(), user.getUserid());
        } else {
            throw new Exception();
        }
    }
}
