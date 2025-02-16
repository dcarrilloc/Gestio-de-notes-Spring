package com.esliceu.services;

import com.google.gson.Gson;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GoogleOAuth2ServiceImpl implements GoogleOAuth2Service{

    @Value("${google-client-id}")
    String clientId;

    @Value("${google-client-secret}")
    String clientSecret;

    @Value("${google-redirect-uri}")
    String redirecturi;

    @Override
    public URL getGoogleRedirectURL() throws Exception {
        URIBuilder b = new URIBuilder("https://accounts.google.com/o/oauth2/v2/auth");
        b.addParameter("scope", "https://www.googleapis.com/auth/userinfo.email");
        b.addParameter("access_type", "offline");
        b.addParameter("state", "state_parameter_passthrough_value");
        b.addParameter("redirect_uri", redirecturi);
        b.addParameter("client_id", clientId);
        b.addParameter("response_type", "code");
        return b.build().toURL();
    }

    @Override
    public String getAccessToken(String code) throws Exception {
        URL url = new URL("https://oauth2.googleapis.com/token");
        Map<String, String> parameters = new HashMap<>();
        parameters.put("client_id", clientId);
        parameters.put("client_secret", clientSecret);
        parameters.put("code", code);
        parameters.put("grant_type", "authorization_code");
        parameters.put("redirect_uri", redirecturi);
        String content = doPost(url, parameters);
        Map<String, Object> map = new Gson().fromJson(content, HashMap.class);
        return map.get("access_token").toString();
    }

    @Override
    public Map<String, String> getUserDetails(String accessToken) throws Exception {
        URIBuilder b = new URIBuilder("https://www.googleapis.com/oauth2/v1/userinfo");
        b.addParameter("access_token", accessToken);
        b.addParameter("alt", "json");

        String resp = doGet(b.build().toURL());
        return new Gson().fromJson(resp, HashMap.class);
    }

    private String doPost(URL url, Map<String, String> parameters) throws Exception {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost post = new HttpPost(url.toString());
        List<NameValuePair> nvps = new ArrayList<>();
        for(String s: parameters.keySet()) {
            nvps.add(new BasicNameValuePair(s, parameters.get(s)));
        }
        post.setEntity(new UrlEncodedFormEntity(nvps));
        CloseableHttpResponse response = httpClient.execute(post);
        response.getEntity();
        return EntityUtils.toString(response.getEntity());
    }

    private String doGet(URL url) throws Exception {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet get = new HttpGet(url.toString());
        CloseableHttpResponse response = httpClient.execute(get);
        response.getEntity();
        return EntityUtils.toString(response.getEntity());
    }

}
