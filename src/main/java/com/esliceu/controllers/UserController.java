package com.esliceu.controllers;

import com.esliceu.entities.User;
import com.esliceu.services.NoteServiceImpl;
import com.esliceu.services.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@Controller
public class UserController {
    @Autowired
    NoteServiceImpl noteService;

    @Autowired
    UserServiceImpl userService;

    @Autowired
    HttpSession session;

    @PostMapping("/login")
    public String userlogin(Model model, @RequestParam(name = "email") String email, @RequestParam(name = "password") String password){
        session.invalidate();
         /* FORM VALIDATION */

        User user = userService.getUserByEmailAndAuthAndPassword(email, "NATIVE", password);

        if(user == null) {
            // Usuario no existe
            model.addAttribute("error", "Usuario no encontrado");
            return "login";
        }

        session.setAttribute("userid", user.getUserid());
        return "redirect:/feed";
    }

    @PostMapping("/register")
    public String userregister(Model model,@RequestParam(name = "username") String username,
                            @RequestParam(name = "email") String email,
                            @RequestParam(name = "password1") String password1,
                            @RequestParam(name = "password2") String password2){

        /* FORM VALIDATION */

        if(password1.equals(password2)) {
            userService.nativeRegister(username, email, "NATIVE", password1);
            User user = userService.getUserByEmailAndAuthAndPassword(email, "NATIVE", password1);
            session.setAttribute("userid", user.getUserid());
            return "redirect:/feed";
        } else {
            model.addAttribute("error", "passwordMismatching");
        }

        return "register";
    }

    @PostMapping("/logout")
    public String userlogout(){
        session.invalidate();
        return "redirect:/";
    }
}
