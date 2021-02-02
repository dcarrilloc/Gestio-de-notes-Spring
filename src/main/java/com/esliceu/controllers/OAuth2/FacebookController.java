package com.esliceu.controllers.OAuth2;

import com.esliceu.services.NoteServiceImpl;
import com.esliceu.services.FacebookOAuth2Service;
import com.esliceu.services.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@Controller
public class FacebookController {

    @Autowired
    NoteServiceImpl noteService;

    @Autowired
    UserServiceImpl userService;

    @Autowired
    HttpSession session;

    @Autowired
    FacebookOAuth2Service facebookOAuth2Service;

    @Value("${facebook-uri}")
    String redirecturi;

    @GetMapping("/facebookLogin")
    public String facebookLogin(Model model) throws Exception  {
        System.out.println("facebooklogin controller");
        model.addAttribute("url", redirecturi);
        return "facebookPopup";
    }

    @GetMapping("/auth/oauth2facebookcallback/")
    public String oauth2facebookcallback(@RequestParam String code) throws Exception {
        System.out.println("En oauth2facebookcallback!!");
        System.out.println("Code: " + code);

        return "login";
    }
}


