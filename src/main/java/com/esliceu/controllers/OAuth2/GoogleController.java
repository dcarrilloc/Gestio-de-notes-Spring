package com.esliceu.controllers.OAuth2;

import com.esliceu.services.GoogleOAuth2Service;
import com.esliceu.services.NoteServiceImpl;
import com.esliceu.services.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.net.URL;
import java.util.Map;

@Controller
public class GoogleController {

    @Autowired
    GoogleOAuth2Service googleOAuth2Service;

    @Autowired
    NoteServiceImpl noteService;

    @Autowired
    UserServiceImpl userService;

    @Autowired
    HttpSession session;

    @GetMapping("/googleLogin")
    public String googleLogin() throws Exception  {
        URL url = googleOAuth2Service.getGoogleRedirectURL();
        return "redirect:" + url;
    }

    @GetMapping("/auth/oauth2callback/")
    public String oauthCallback(@RequestParam String code) throws Exception {
        String accessToken = googleOAuth2Service.getAccessToken(code);
        Map<String,String> userDetails = googleOAuth2Service.getUserDetails(accessToken);

        // Check if user is logged. If not, register user...
        Long userid = userService.checkIfUserIsLogged(userDetails.get("email"), "GOOGLE");
        if(userid != null) {
            session.setAttribute("userid", userid);
        } else {
            Long id = userService.oAuth2Register(userDetails.get("email"), "GOOGLE");
            session.setAttribute("userid", id);
        }

        return "redirect:/feed";
    }
}
