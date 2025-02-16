package com.esliceu.controllers;

import com.esliceu.services.NoteServiceImpl;
import com.esliceu.services.UserServiceImpl;
import com.esliceu.services.VersionServiceImpl;
import com.esliceu.utils.exceptions.Note.NoteNotFound;
import com.esliceu.utils.exceptions.Note.UnauthorizedNote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
public class RoutingController {
    @Autowired
    NoteServiceImpl noteService;

    @Autowired
    UserServiceImpl userService;

    @Autowired
    VersionServiceImpl versionService;

    @Autowired
    HttpSession session;

    @GetMapping("/login")
    public String login() { return "login"; }

    @GetMapping("/register")
    public String register() { return "register"; }

    @GetMapping("/feed")
    public String feed(Model model, @RequestParam(value = "page", required = false) Integer pagination,
                       @RequestParam(value = "searchType", required = false) String searchType,
                       @RequestParam(value = "searchValue", required = false) String searchValue,
                       @RequestParam(value = "dates", required = false) String dates) {
        Long sessionUserId = (Long) session.getAttribute("userid");

        int page;
        if(pagination == null) {
            page = 0;
        } else {
            page = --pagination ;
        }

        if(searchType == null) {
            searchType = "lastModDate-desc";
        }

        String column = searchType.split("-")[0];
        String order = searchType.split("-")[1];

        if(dates == null) {
            dates = "02/01/2021 - 02/28/2021";
        }

        // Transform String '11/01/2020 - 12/15/2020' into two Dates.
        String date1 = dates.substring(0, 10);
        String date2 = dates.substring(13, 23);

        //Change '/' to '-' in dates
        String date1temp = date1.replace('/', '-');
        String date2temp = date2.replace('/', '-');

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        LocalDateTime dateFrom = LocalDate.parse(date1temp, formatter).atStartOfDay();
        LocalDateTime dateTo = LocalDate.parse(date2temp, formatter).atStartOfDay();

        if(searchValue == null) {
            searchValue = "";
        }

        int totalNotes = noteService.getFeedNotesByUser(sessionUserId, dateFrom, dateTo, searchValue).size();
        int totalPages = (int) Math.ceil((totalNotes - 1)/10);

        model.addAttribute("inputValue", searchValue);
        model.addAttribute("inputType", searchType);
        model.addAttribute("dates", dates);

        model.addAttribute("totalPages", totalPages);
        model.addAttribute("notes", noteService.getPagedNotes(sessionUserId, dateFrom, dateTo, searchValue, page, column, order));

        if(page == 0) {
            model.addAttribute("nextPage", 2);
            model.addAttribute("previousPageAvailable", false);
        } else if(page == (Integer) model.getAttribute("totalPages")) {
            model.addAttribute("nextPageAvailable", false);
        } else {
            model.addAttribute("previousPage", page - 1);
            model.addAttribute("nextPage", page + 1);
            model.addAttribute("previousPageAvailable", true);
            model.addAttribute("nextPageAvailable", true);
        }

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

    @GetMapping("/detailNote/{id}")
    public String detailNote(Model model, @PathVariable Long id) {
        Long sessionUserId = (Long) session.getAttribute("userid");
        model.addAttribute("username", userService.getUserById(sessionUserId).getUsername());
        Long noteid = versionService.getNoteFromVersionId(id).getNoteid();

        if(noteService.checkNoteOwnership(sessionUserId, noteid) || noteService.checkPermission(sessionUserId, noteid).equals("EDITOR")) {
            if(noteService.checkNoteOwnership(sessionUserId, noteid)) {
                model.addAttribute("ownership", true);
            } else {
                model.addAttribute("permissionMode", "EDITOR");
            }
            model.addAttribute("usersShared", noteService.getSharedUsersFromNote(sessionUserId, noteid));
            model.addAttribute("versionList", versionService.getVersionFromNote(noteid));
        } else {
            // If user not owns the note cannot edit, just see (if it's shared).
            // Checking if note is shared with the user in view mode...
            if(noteService.checkNoteIsSharedByUserId(sessionUserId, noteid) && noteService.checkPermission(sessionUserId, noteid).equals("VIEW")) {
                // Note shared with user in view mode
                model.addAttribute("permissionMode", "VIEW");
            } else {
                // Note NOT shared with user
                throw new UnauthorizedNote();
            }
        }
        model.addAttribute("note", noteService.getNoteById(noteid));
        model.addAttribute("version", versionService.getParsedBody(id, true));

        return "detailNote";
    }

    @GetMapping("/edit/{id}")
    public String editNote(Model model, @PathVariable Long id) {
        Long sessionUserId = (Long) session.getAttribute("userid");
        model.addAttribute("username", userService.getUserById(sessionUserId).getUsername());
        Long noteid = versionService.getNoteFromVersionId(id).getNoteid();


        // If user owns the note, he can edit it. If not, return to detailNote.
        if(noteService.checkNoteOwnership(sessionUserId, noteid) || noteService.checkPermission(sessionUserId, noteid).equals("EDITOR")) {
            model.addAttribute("note", noteService.getNoteById(noteid));
            model.addAttribute("version", versionService.getVersionFromId(id));
            return "markdown";
        }

        throw new UnauthorizedNote();
    }

    @GetMapping("/profile")
    public String userProfile(Model model) {
        Long sessionUserId = (Long) session.getAttribute("userid");
        String authMethod = userService.getUserById(sessionUserId).getAuth();
        model.addAttribute("user", userService.getUserById(sessionUserId));
        model.addAttribute("authMethod", authMethod);
        return "userProfile";
    }
}
