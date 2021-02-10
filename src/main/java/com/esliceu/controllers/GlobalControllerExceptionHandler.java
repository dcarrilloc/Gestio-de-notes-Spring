package com.esliceu.controllers;

import com.esliceu.utils.exceptions.Note.NoteException;
import com.esliceu.utils.exceptions.Note.NoteNotFound;
import com.esliceu.utils.exceptions.Note.UnauthorizedNote;
import com.esliceu.utils.exceptions.User.UserException;
import com.esliceu.utils.exceptions.User.UserNotFound;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalControllerExceptionHandler {

    @ExceptionHandler(UnauthorizedNote.class)
    public String unauthorizedNote(Model model, UnauthorizedNote err) {
        model.addAttribute("err", err.getMessage());
        return "error";
    }

    @ExceptionHandler(NoteNotFound.class)
    public String noteNotFound(Model model, NoteNotFound err) {
        model.addAttribute("err", err.getMessage());
        return "error";
    }

    @ExceptionHandler(NoteException.class)
    public String noteException(Model model, NoteException err) {
        model.addAttribute("err", err.getMessage());
        return "error";
    }

    @ExceptionHandler(UserNotFound.class)
    public String userNotFound(Model model, UserNotFound err) {
        model.addAttribute("err", err.getMessage());
        return "error";
    }

    @ExceptionHandler(UserException.class)
    public String userException(Model model, UserException err) {
        model.addAttribute("err", err.getMessage());
        return "error";
    }

    @ExceptionHandler(Exception.class)
    public String handleConflict(Exception err, Model model) {
        model.addAttribute("err", "Something went wrong. Try again later!");
        err.printStackTrace();
        return "error";
    }

}
