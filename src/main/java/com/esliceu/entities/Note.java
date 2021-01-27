package com.esliceu.entities;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name="note")
public class Note {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long noteid;
    String title;
    String body;
    LocalDateTime creationDate;
    LocalDateTime lastModDate;

    @ManyToOne(fetch = FetchType.EAGER)
    User owner;


}
