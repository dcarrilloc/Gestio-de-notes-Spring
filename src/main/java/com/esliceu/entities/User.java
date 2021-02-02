package com.esliceu.entities;

import javax.persistence.*;
import java.util.Set;

@Table(name = "User",
        uniqueConstraints = { @UniqueConstraint( columnNames = { "email", "auth" } ) } )
@Entity(name="User")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userid;

    @Column(unique = true)
    private String username;
    private String email;
    private String auth;
    private String password;


    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = true)
    private Set<Note> notes;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private Set<Shared_Note> sharedNotes;

    public Set<Shared_Note> getSharedNotes() {
        return sharedNotes;
    }

    public void setSharedNotes(Set<Shared_Note> sharedNotes) {
        this.sharedNotes = sharedNotes;
    }

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Note> getNotes() {
        return notes;
    }

    public void setNotes(Set<Note> notes) {
        this.notes = notes;
    }

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }

    @Override
    public String toString() {
        return "User{" +
                "userid=" + userid +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", auth='" + auth + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
