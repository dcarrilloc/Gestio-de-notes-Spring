package com.esliceu.controllers.OAuth2;

import com.esliceu.services.FacebookOAuth2Service;
import com.esliceu.services.NoteServiceImpl;
import com.esliceu.services.TwitterOAuth2Service;
import com.esliceu.services.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@Controller
public class TwitterController {

    @Autowired
    NoteServiceImpl noteService;

    @Autowired
    UserServiceImpl userService;

    @Autowired
    HttpSession session;

    @Autowired
    TwitterOAuth2Service twitterOAuth2Service;


    @GetMapping("/twitterLogin")
    public String twitterlogin(Model model) throws Exception  {
        System.out.println("twitterlogin controller");
        twitterOAuth2Service.getRequestToken();
        return "login";
    }

    @GetMapping("/auth/oauth2Twittercallback/")
    public String oauth2twittercallback() throws Exception {
        System.out.println("En oauth2twittercallback!!");
        System.out.println("Code: ");

        return "login";
    }


}


