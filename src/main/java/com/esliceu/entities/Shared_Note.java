package com.esliceu.entities;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name = "Shared_note")
public class Shared_Note {
    @EmbeddedId
    Shared_NoteCK id;

    @ManyToOne
    @MapsId("noteid")
    @JoinColumn(name = "note_noteid")
    private Note note;

    @ManyToOne
    @MapsId("userid")
    @JoinColumn(name = "user_userid")
    private User user;

    private String permissionMode;

    private LocalDateTime sharedDate;

    public Shared_NoteCK getId() {
        return id;
    }

    public void setId(Shared_NoteCK id) {
        this.id = id;
    }

    public Note getNote() {
        return note;
    }

    public void setNote(Note note) {
        this.note = note;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getSharedDate() {
        return sharedDate;
    }

    public void setSharedDate(LocalDateTime sharedDate) {
        this.sharedDate = sharedDate;
    }

    public String getPermissionMode() {
        return permissionMode;
    }

    public void setPermissionMode(String permissionMode) {
        this.permissionMode = permissionMode;
    }

    @Override
    public String toString() {
        return "Shared_Note{" +
                "id=" + id +
                ", note=" + note +
                ", user=" + user +
                ", sharedDate=" + sharedDate +
                '}';
    }
}
