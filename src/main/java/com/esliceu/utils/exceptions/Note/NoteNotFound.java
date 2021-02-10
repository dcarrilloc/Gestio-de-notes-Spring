package com.esliceu.utils.exceptions.Note;

public class NoteNotFound extends NoteException {
    public NoteNotFound() {
        super("Requested note not found. Try again later.");
    }
}
