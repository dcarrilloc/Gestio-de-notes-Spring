package com.esliceu.controllers.OAuth2;

import com.esliceu.services.NoteServiceImpl;
import com.esliceu.services.FacebookOAuth2Service;
import com.esliceu.services.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

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

    @GetMapping("/facebookLogin")
    public String facebookLogin() throws Exception  {
        //String resp = facebookOAuth2Service.getRequestToken();
        //System.out.println("Response in controller: " + resp);
        //return "redirect:/login";
        return "";
    }

    @GetMapping("/auth/oauth2facebookcallback/")
    public String oauth2facebookcallback() throws Exception {
        System.out.println("En oauth2facebookcallback!!");
        /*

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

         */
        return "login";
    }
}
