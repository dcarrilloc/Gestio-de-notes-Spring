package com.esliceu.utils.exceptions.Note;

public class UnauthorizedNote extends NoteException {
    public UnauthorizedNote() {
        super("You were not allowed to see this note.");
    }
}
