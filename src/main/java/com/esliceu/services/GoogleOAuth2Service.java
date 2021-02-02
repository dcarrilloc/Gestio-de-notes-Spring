package com.esliceu.services;

import java.net.URL;
import java.util.Map;

public interface GoogleOAuth2Service {

    public URL getGoogleRedirectURL() throws Exception;
    public String getAccessToken(String code) throws Exception;
    public Map<String, String> getUserDetails(String accessToken) throws Exception;



}
