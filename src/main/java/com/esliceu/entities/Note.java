package com.esliceu.entities;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity(name="Note")
public class Note {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long noteid;
    private String title;

    @Type(type="text")
    private String body;
    private LocalDateTime creationDate;
    private LocalDateTime lastModDate;

    @ManyToOne(optional = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User owner;

    @OneToMany(mappedBy = "note", fetch = FetchType.EAGER)
    private Set<Shared_Note> sharedNotes;



    public Set<Shared_Note> getSharedNotes() {
        return sharedNotes;
    }

    public void setSharedNotes(Set<Shared_Note> sharedNotes) {
        this.sharedNotes = sharedNotes;
    }

    public Long getNoteid() {
        return noteid;
    }

    public void setNoteid(Long noteid) {
        this.noteid = noteid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDateTime getLastModDate() {
        return lastModDate;
    }

    public void setLastModDate(LocalDateTime lastModDate) {
        this.lastModDate = lastModDate;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    @Override
    public String toString() {
        return "Note{" +
                "noteid=" + noteid +
                ", title='" + title + '\'' +
                ", body='" + body + '\'' +
                ", creationDate=" + creationDate +
                ", lastModDate=" + lastModDate +
                '}';
    }
}
