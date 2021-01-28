package com.esliceu.services;

import com.esliceu.entities.Note;
import com.esliceu.repos.NoteRepo;
import com.esliceu.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteServiceImpl implements NoteService {
    @Autowired
    NoteRepo noteRepo;

    @Autowired
    UserRepo userRepo;


    public List<Note> getAllNotes() {
        return noteRepo.findAll();
    }



}
