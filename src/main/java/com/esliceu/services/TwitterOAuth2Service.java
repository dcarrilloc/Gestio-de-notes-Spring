package com.esliceu.services;

import java.net.URL;
import java.util.HashMap;

public interface TwitterOAuth2Service {
    URL getRequestToken() throws Exception;
    HashMap getAccessToken(String oauth_token, String oauth_verifier) throws Exception;
}
