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
import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

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
    public String twitterlogin() throws Exception  {
        URL url = twitterOAuth2Service.getRequestToken();
        return "redirect:" + url;
    }

    @GetMapping("/auth/oauth2Twittercallback/")
    public String oauth2twittercallback(@RequestParam String oauth_token, @RequestParam String oauth_verifier) throws Exception {
        HashMap userDetails = twitterOAuth2Service.getAccessToken(oauth_token, oauth_verifier);

        // Check if user is logged. If not, register user...
        session.invalidate();
        Long userid = userService.checkIfUserIsLogged((String) userDetails.get("email"), "TWITTER");
        if(userid != null) {
            session.setAttribute("userid", userid);
        } else {
            Long id = userService.oAuth2Register((String) userDetails.get("email"), "TWITTER");
            session.setAttribute("userid", id);
        }

        return "redirect:/feed";
    }
}


