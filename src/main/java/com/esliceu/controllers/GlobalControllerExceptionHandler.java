package com.esliceu.controllers;

import com.esliceu.utils.exceptions.Note.NoteNotFound;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalControllerExceptionHandler {

    @ExceptionHandler(NoteNotFound.class)
    public String noteNotFound() {
        // Nothing to do
        System.out.println("Note not found");
        return "login";
    }

    @ExceptionHandler(Exception.class)
    public String handleConflict(Exception e) {
        // Nothing to do
        e.printStackTrace();
        return "login";
    }

}
