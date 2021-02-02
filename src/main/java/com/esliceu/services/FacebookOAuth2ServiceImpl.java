package com.esliceu.services;

import org.apache.http.HttpHeaders;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class FacebookOAuth2ServiceImpl implements FacebookOAuth2Service {

    @Value("${twitter-redirect-uri}")
    String redirecturi;

    @Override
    public String getRequestToken() throws Exception {
        /*
        URL url = new URL("https://api.twitter.com/oauth/request_token");
        Map<String, String> parameters = new HashMap<>();
        parameters.put("oauth_callback", redirecturi);
        StringBuilder authorization = new StringBuilder();
        authorization.append("OAuth ");
        authorization.append("oauth_nonce=")
        String content = doPost(url, parameters);
        System.out.println("response: " + content);
        Map<String, Object> map = new Gson().fromJson(content, HashMap.class);
        System.out.println("mapped resp: " + map.toString());
        return content;
        */
        return "";
    }


    private String doPost(URL url, Map<String, String> parameters, String authorization) throws Exception {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost post = new HttpPost(url.toString());
        List<NameValuePair> nvps = new ArrayList<>();
        for(String s: parameters.keySet()) {
            nvps.add(new BasicNameValuePair(s, parameters.get(s)));
        }
        post.setEntity(new UrlEncodedFormEntity(nvps));
        post.setHeader(HttpHeaders.AUTHORIZATION, authorization);
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
