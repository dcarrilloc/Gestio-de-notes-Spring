package com.esliceu.controllers;

import com.esliceu.services.NoteServiceImpl;
import com.esliceu.services.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpSession;

@Controller
public class RoutingController {
    @Autowired
    NoteServiceImpl noteService;

    @Autowired
    UserServiceImpl userService;

    @Autowired
    HttpSession session;

    @GetMapping("/login")
    public String login() { return "login"; }

    @GetMapping("/register")
    public String register() { return "register"; }

    @GetMapping("/feed")
    public String feed(Model model) {
        Long sessionUserId = (Long) session.getAttribute("userid");
        model.addAttribute("notes", noteService.getFeedNotesByUser(sessionUserId));
        model.addAttribute("username", userService.getUserById(sessionUserId).getUsername());

        return "feed";
    }

    @GetMapping("/createNote")
    public String createNote(Model model) {
        Long sessionUserId = (Long) session.getAttribute("userid");
        model.addAttribute("username", userService.getUserById(sessionUserId).getUsername());
        return "detailNote";
    }

    @GetMapping("/detailNote")
    public String detailNote(Model model, @RequestParam Long id) {
        Long sessionUserId = (Long) session.getAttribute("userid");
        model.addAttribute("username", userService.getUserById(sessionUserId).getUsername());

        if(id != null) {
            if(noteService.checkNoteOwnership(sessionUserId, id)) {
                // If user owns the note can edit
                model.addAttribute("ownership", true);
                // req.setAttribute("usersShared", noteService.getSharedUsersFromNote(loggedUserId, noteid));
            } else {
                // If user not owns the note cannot edit, just see (if it's shared).
                // Checking if note is shared with the user.

            }
            model.addAttribute("note", noteService.getParsedNote(id, true));
        }

        return "detailNote";
    }

    @GetMapping("/edit")
    public String editNote(Model model, @RequestParam Long id) {
        Long sessionUserId = (Long) session.getAttribute("userid");
        model.addAttribute("username", userService.getUserById(sessionUserId).getUsername());

        // If user owns the note, he can edit it. If not, return to detailNote.
        if(noteService.checkNoteOwnership(sessionUserId, id)) {
            model.addAttribute("note", noteService.getNoteById(id));
            return "markdown";
        }

        return "redirect:/detailNote?id=" + id;
    }
}
