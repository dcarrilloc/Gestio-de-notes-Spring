package com.esliceu.entities;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name="Note")
public class Note {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long noteid;
    private String title;
    private String body;
    private LocalDateTime creationDate;
    private LocalDateTime lastModDate;

    @ManyToOne(optional = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "owner", nullable = false)
    private User owner;

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
