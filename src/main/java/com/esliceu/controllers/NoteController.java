package com.esliceu.controllers;

import com.esliceu.services.NoteServiceImpl;
import com.esliceu.services.UserServiceImpl;
import com.esliceu.services.VersionService;
import com.esliceu.services.VersionServiceImpl;
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
    VersionServiceImpl versionService;

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
    public String deleteNote(@RequestParam(name = "notesToDelete") Long[] noteList){
        Long userid = (Long) session.getAttribute("userid");
        noteService.deleteNote(userid, noteList);
        return "redirect:/feed";
    }

    @PostMapping("/shareNote")
    public String shareNote(@RequestParam(name = "option") String user, @RequestParam(name = "noteid") Long noteid, @RequestParam(name = "permissionMode") String permissionMode) throws Exception {
        Long ownerid = (Long) session.getAttribute("userid");

        if(permissionMode.equals("EDITOR") || permissionMode.equals("VIEW")) {
            noteService.shareNote(user, noteid, ownerid, permissionMode);
        } else if(permissionMode.equals("TRANSFER_OWNER"))
            noteService.transferOwnership(noteid, user);
        return "redirect:/feed";
    }

    @PostMapping("/deleteUserFromSharedNote")
    public String deleteUserFromSharedNote(@RequestParam(name = "userid") Long userid, @RequestParam(name = "noteid") Long noteid){
        Long ownerid = (Long) session.getAttribute("userid");
        noteService.deleteUserFromSharedNote(noteid, ownerid, userid);
        return "redirect:/feed";
    }

    @PostMapping("/copyNote")
    public String copyNote(@RequestParam(name = "versionid") Long versionid) throws Exception {
        Long userid = (Long) session.getAttribute("userid");
        versionService.makeCopy(versionid, userid);
        return "redirect:/feed";
    }
}


