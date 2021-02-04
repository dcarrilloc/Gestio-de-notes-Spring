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
import java.security.NoSuchAlgorithmException;

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

    @PostMapping("/profile")
    public String profile(@RequestParam(name = "username", required = false) String username,
                          @RequestParam(name = "email", required = false) String email,
                          @RequestParam(name = "pass1", required = false) String pass1,
                          @RequestParam(name = "pass2", required = false) String pass2,
                          Model model) throws NoSuchAlgorithmException {

        Long sessionid = (Long) session.getAttribute("userid");
        User user = userService.getUserById(sessionid);

        if (username == null) {
            username = user.getUsername();
        }

        if (email == null) {
            email = user.getEmail();
        }

        String authMethod = user.getAuth();
        model.addAttribute("authMethod", authMethod);
        model.addAttribute("user", user);

        User existent = userService.getUserByUsername(username);
        if(existent != null && (!user.getUserid().equals(existent.getUserid()))) {
            model.addAttribute("status", 6);
            model.addAttribute("usernameValidation", "is-invalid");
            model.addAttribute("emailValidation", "is-valid");
            model.addAttribute("passwordValidation", "is-invalid");
            return "userProfile";
        }


        short code = userService.checkRegisterCredentials(username, email, pass1, pass2, authMethod);

        if (authMethod.equals("NATIVE")) {
            if (pass1 == null || pass2 == null) {
                model.addAttribute("status", 8);
                model.addAttribute("usernameValidation", "is-valid");
                model.addAttribute("emailValidation", "is-valid");
                model.addAttribute("passwordValidation", "is-invalid");
                return "userProfile";
            } else if (!pass1.equals(pass2)) {
                model.addAttribute("status", 4);
                model.addAttribute("usernameValidation", "is-valid");
                model.addAttribute("emailValidation", "is-valid");
                model.addAttribute("passwordValidation", "is-invalid");
                return "userProfile";
            } else if (code == 3) {
                model.addAttribute("status", 3);
                model.addAttribute("usernameValidation", "is-valid");
                model.addAttribute("emailValidation", "is-valid");
                model.addAttribute("passwordValidation", "is-invalid");
                return "userProfile";
            } else if (code == 4) {
                model.addAttribute("status", 4);
                model.addAttribute("usernameValidation", "is-valid");
                model.addAttribute("emailValidation", "is-valid");
                model.addAttribute("passwordValidation", "is-invalid");
                return "userProfile";
            } else if (code == 5) {
                model.addAttribute("status", 5);
                model.addAttribute("usernameValidation", "is-valid");
                model.addAttribute("emailValidation", "is-valid");
                model.addAttribute("passwordValidation", "is-invalid");
                return "userProfile";
            }
            userService.updateNativeUser(username, email, pass1, sessionid);
        }

        // pendiente de encriptacion de password
        /*
        if(username.equals(user.getUsername()) && email.equals(user.getEmail()) && pass.equals(user.getPassword())) {
            model.addAttribute("status", 7);
            model.addAttribute("usernameValidation", "is-invalid");
            model.addAttribute("emailValidation", "is-invalid");
            model.addAttribute("passwordValidation", "is-invalid");
            return "userProfile";
        }
         */

        userService.updateOAuthUser(username, sessionid);
        return "redirect:/feed";
    }
}






















