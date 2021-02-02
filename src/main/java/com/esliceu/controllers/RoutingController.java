package com.esliceu.controllers;

import com.esliceu.services.NoteServiceImpl;
import com.esliceu.services.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

        String username = userService.getUserById(sessionUserId).getUsername();
        if(username.equals("")) {
            model.addAttribute("username", userService.getUserById(sessionUserId).getEmail());
        } else {
            model.addAttribute("username", username);
        }

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

        if(noteService.checkNoteOwnership(sessionUserId, id) || noteService.checkPermission(sessionUserId, id).equals("EDITOR")) {
            if(noteService.checkNoteOwnership(sessionUserId, id)) {
                model.addAttribute("ownership", true);
            } else {
                model.addAttribute("permissionMode", "EDITOR");
            }
            model.addAttribute("usersShared", noteService.getSharedUsersFromNote(sessionUserId, id));
        } else {
            // If user not owns the note cannot edit, just see (if it's shared).
            // Checking if note is shared with the user in view mode...
            System.out.println("Comprobamos que la nota esté compartida con el usuario y que sea de tipo view");
            System.out.println(noteService.checkNoteIsSharedByUserId(sessionUserId, id));
            System.out.println(noteService.checkPermission(sessionUserId, id).equals("VIEW"));
            if(noteService.checkNoteIsSharedByUserId(sessionUserId, id) && noteService.checkPermission(sessionUserId, id).equals("VIEW")) {
                // Note shared with user in view mode
                model.addAttribute("permissionMode", "VIEW");
            } else {
                // Note NOT shared with user
                return "redirect:/feed";
            }
        }
        model.addAttribute("note", noteService.getParsedNote(id, true));

        return "detailNote";
    }

    @GetMapping("/edit")
    public String editNote(Model model, @RequestParam Long id) {
        Long sessionUserId = (Long) session.getAttribute("userid");
        model.addAttribute("username", userService.getUserById(sessionUserId).getUsername());

        // If user owns the note, he can edit it. If not, return to detailNote.
        if(noteService.checkNoteOwnership(sessionUserId, id) || noteService.checkPermission(sessionUserId, id).equals("EDITOR")) {
            model.addAttribute("note", noteService.getNoteById(id));
            return "markdown";
        }

        return "redirect:/detailNote?id=" + id;
    }

    @GetMapping("/profile")
    public String userProfile(Model model) {
        Long sessionUserId = (Long) session.getAttribute("userid");

        model.addAttribute("user", userService.getUserById(sessionUserId));
        return "userProfile";
    }
}
