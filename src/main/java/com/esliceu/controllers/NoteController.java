package com.esliceu.controllers;

import com.esliceu.services.NoteServiceImpl;
import com.esliceu.services.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@Controller
public class NoteController {
    @Autowired
    NoteServiceImpl noteService;

    @Autowired
    UserServiceImpl userService;

    @Autowired
    HttpSession session;

    @PostMapping("/createNote")
    public String createNote(@RequestParam(name = "title") String title, @RequestParam(name = "body") String body){
        Long userid = (Long) session.getAttribute("userid");
        noteService.saveNote(null, title, body, userid);
        return "redirect:/feed";
    }

    @PostMapping("/saveNote")
    public String saveNote(@RequestParam(name = "noteid") Long noteid, @RequestParam(name = "title") String title, @RequestParam(name = "body") String body){
        Long userid = (Long) session.getAttribute("userid");
        noteService.saveNote(noteid, title, body, userid);
        return "redirect:/feed";
    }

    @PostMapping("/deleteNote")
    public String deleteNote(@RequestParam(name = "noteid") Long noteid){
        Long userid = (Long) session.getAttribute("userid");
        noteService.deleteNote(userid, noteid);
        return "redirect:/feed";
    }
}









