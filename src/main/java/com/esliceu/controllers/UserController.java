package com.esliceu.controllers;

import com.esliceu.entities.User;
import com.esliceu.services.NoteServiceImpl;
import com.esliceu.services.UserServiceImpl;
import com.esliceu.utils.exceptions.User.UserNotFound;
import com.google.common.cache.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpSession;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@Controller
public class UserController {
    @Autowired
    NoteServiceImpl noteService;

    @Autowired
    UserServiceImpl userService;

    @Autowired
    HttpSession session;

    @PostMapping("/login")
    public String userlogin(Model model,
                            @RequestParam(name = "email") String email,
                            @RequestParam(name = "password") String password,
                            @RequestParam(name = "_csrftoken") String csrfToken) throws Exception {
        session.invalidate();
        short status;
        try {
            status = userService.checkLoginCredentials(email, password);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
            throw new Exception();
        }

        switch (status) {
            case 0: {
                // Correct auth
                Long userid = userService.getUserByEmailAndAuth(email, "NATIVE").getUserid();
                session.setAttribute("userid", userid);
                return "redirect:/feed";
            }
            case 1: {
                // 1: user not found on ddbb
                model.addAttribute("status", 1);
                model.addAttribute("usernameValidation", "is-invalid");
                model.addAttribute("password", "is-invalid");
                model.addAttribute("csrfToken", csrfToken);
                return "login";
            }
            case 2: {
                // 2: user found but incorrect password.
                model.addAttribute("status", 2);
                model.addAttribute("usernameValidation", " is-valid");
                model.addAttribute("password", "is-invalid");
                model.addAttribute("csrfToken", csrfToken);
                return "login";
            }
            case 3: {
                // 3: something went wrong.
                throw new Exception();
            }
        }
        throw new Exception();
    }

    @PostMapping("/register")
    public String userregister(@RequestParam(name = "username", required = false) String username,
                               @RequestParam(name = "email", required = false) String email,
                               @RequestParam(name = "password1", required = false) String password1,
                               @RequestParam(name = "password2", required = false) String password2,
                               @RequestParam(name = "_csrftoken") String csrfToken,
                               Model model) throws Exception {

        short status = userService.checkRegisterCredentials(username, email, password1, password2, "NATIVE");

        switch (status) {
            case 0: {
                // Correct syntax. Creating new user
                Long createdUserId = userService.nativeRegister(username, email, "NATIVE", password1);
                if (createdUserId != null) {
                    // Login user
                    model.addAttribute("username", username);
                    session.setAttribute("userid", createdUserId);
                    return "redirect:/feed";
                } else {
                    // Username exists
                    model.addAttribute("status", 4);
                    model.addAttribute("usernameValidation", "is-invalid");
                    model.addAttribute("password", "is-invalid");
                    model.addAttribute("csrfToken", csrfToken);
                    return "register";
                }
            }
            case 1: {
                // 1: invalid username. Regexp fail.
                model.addAttribute("status", 1);
                model.addAttribute("usernameValidation", "is-invalid");
                model.addAttribute("password", "is-invalid");
                model.addAttribute("csrfToken", csrfToken);
                return "register";
            }
            case 2: {
                // 2: invalid email. Regexp fail.
                model.addAttribute("status", 2);
                model.addAttribute("usernameValidation", "is-valid");
                model.addAttribute("emailValidation", "is-invalid");
                model.addAttribute("csrfToken", csrfToken);
                return "register";
            }
            case 3: {
                // 3: invalid password. Regexp fail.
                model.addAttribute("status", 3);
                model.addAttribute("usernameValidation", "is-valid");
                model.addAttribute("emailValidation", "is-valid");
                model.addAttribute("passwordValidation", "is-invalid");
                model.addAttribute("csrfToken", csrfToken);
                return "register";
            }
            case 4: {
                // 4. Both password input does not match.
                model.addAttribute("status", 4);
                model.addAttribute("usernameValidation", "is-valid");
                model.addAttribute("emailValidation", "is-valid");
                model.addAttribute("passwordValidation", "is-invalid");
                model.addAttribute("csrfToken", csrfToken);
                return "register";
            }
            case 5: {
                // 5. Password must not be the same as username.
                model.addAttribute("status", 5);
                model.addAttribute("usernameValidation", "is-valid");
                model.addAttribute("emailValidation", "is-valid");
                model.addAttribute("passwordValidation", "is-invalid");
                model.addAttribute("csrfToken", csrfToken);
                return "register";
            }
        }
        throw new Exception();
    }

    @PostMapping("/logout")
    public String userlogout() {
        session.invalidate();
        return "redirect:/";
    }

    @PostMapping("/profile")
    public String profile(@RequestParam(name = "username", required = false) String username,
                          @RequestParam(name = "email", required = false) String email,
                          @RequestParam(name = "pass1", required = false) String pass1,
                          @RequestParam(name = "pass2", required = false) String pass2,
                          Model model) throws Exception {

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
        if (existent != null && (!user.getUserid().equals(existent.getUserid()))) {
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

    @PostMapping("/deleteAccount")
    public String deleteAccount() {
        Long userid = userService.getUserById((Long) session.getAttribute("userid")).getUserid();
        session.invalidate();
        userService.deleteAccount(userid);
        return "redirect:/";
    }
}






















