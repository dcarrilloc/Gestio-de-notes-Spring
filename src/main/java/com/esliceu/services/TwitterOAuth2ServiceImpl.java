package com.esliceu.services;

import com.google.gson.Gson;
import org.apache.commons.codec.binary.Hex;
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

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.*;

@Service
public class TwitterOAuth2ServiceImpl implements TwitterOAuth2Service {

    @Value("${twitter-callback-uri}")
    String redirecturi;

    @Value("${twitter-api-key}")
    String apiKey;

    @Value("${twitter-api-key-secret}")
    String apiKeySecret;

    @Value("${twitter-bearer-token}")
    String bearerToken;

    @Value("${twitter-access-token}")
    String accessToken;

    @Value("${twitter-access-token-secret}")
    String accessTokenSecret;



    /*
        TWITTER LOGIN FLOW


        1er paso: REQUEST TOKEN --> POST oauth/request_token
            - Devuelve oauth_token, oauth_token_secret, oauth_callback_confirmed

        2do paso: Redirecting the user --> GET oauth/authenticate

                    Cuando el usuario se ha autenticado, hace un GET a la callback URL con:
                        - oauth_token
                        - oauth_verifier

        3r paso: convertir request_token a access_token. --> POST oauth/access_token
            - Devuelve oauth_token, oauth_token_secret
            - Con oauth_token y oauth_token_secret coger datos del usuario --> GET account/verify_credentials


     */


    @Override
    public String getRequestToken() throws Exception {
        URL url = new URL("https://api.twitter.com/oauth/request_token");
        String oauth_signature = buildSignature("POST", url, "");
        System.out.println("Signature: " + oauth_signature);

        Timestamp timestamp = new Timestamp(System.currentTimeMillis()/1000);

        Map<String, String> parameters = new HashMap<>();

        NavigableMap<String, String> authorization = new TreeMap<>();
        authorization.put("oauth_callback", URLEncoder.encode(redirecturi, "UTF-8"));
        authorization.put("oauth_consumer_key", apiKey);
        authorization.put("oauth_nonce", generateNonce());
        authorization.put("oauth_signature", oauth_signature);
        authorization.put("oauth_signature_method", "HMAC-SHA1");
        authorization.put("oauth_timestamp", String.valueOf(timestamp.getTime()));
        authorization.put("oauth_version", "1.0");

        String response = doPost(url, parameters, authorization, "OAuth ");
        System.out.println("Response: " + response);
        return "";
    }

    private String buildSignature(String method, URL url, String tokenSecret) throws MalformedURLException, UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis()/1000);

        NavigableMap<String, String> parameters = new TreeMap<>();
        parameters.put("oauth_callback", redirecturi);
        parameters.put("oauth_consumer_key", apiKey);
        parameters.put("oauth_nonce", generateNonce());
        parameters.put("oauth_signature_method", "HMAC-SHA1");
        parameters.put("oauth_timestamp", String.valueOf(timestamp.getTime()));
        parameters.put("oauth_version", "1.0");

        StringBuilder parameterString = new StringBuilder();
        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            parameterString.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            parameterString.append("=");
            parameterString.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            if(!entry.getKey().equals(parameters.lastKey())) {
                parameterString.append("&");
            }
        }

        StringBuilder signaturaBaseString = new StringBuilder();
        signaturaBaseString.append(method);
        signaturaBaseString.append("&");
        signaturaBaseString.append(URLEncoder.encode(String.valueOf(url), "UTF-8"));
        signaturaBaseString.append("&");
        signaturaBaseString.append(URLEncoder.encode(String.valueOf(parameterString), "UTF-8"));

        StringBuilder signingKeysb = new StringBuilder();
        signingKeysb.append(URLEncoder.encode(apiKey, "UTF-8"));
        signingKeysb.append("&");
        signingKeysb.append(URLEncoder.encode(tokenSecret, "UTF-8"));

        return hmacSha1(String.valueOf(signaturaBaseString), String.valueOf(signingKeysb));
    }

    private String generateNonce() {
        String saltchars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 10) {
            int index = (int) (rnd.nextFloat() * saltchars.length());
            salt.append(saltchars.charAt(index));
        }
        return salt.toString();
    }

    public static String hmacSha1(String value, String key) {
        try {
            // Get an hmac_sha1 key from the raw key bytes
            byte[] keyBytes = key.getBytes();
            SecretKeySpec signingKey = new SecretKeySpec(keyBytes, "HmacSHA1");

            // Get an hmac_sha1 Mac instance and initialize with the signing key
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(signingKey);

            // Compute the hmac on input data bytes
            byte[] rawHmac = mac.doFinal(value.getBytes());

            // Convert raw bytes to Hex
            byte[] hexBytes = new Hex().encode(rawHmac);

            //  Covert array of Hex bytes to a String
            return new String(hexBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String doPost(URL url, Map<String, String> parameters, NavigableMap<String, String> authorization, String authMethod) throws Exception {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost post = new HttpPost(url.toString());

        List<NameValuePair> nvps = new ArrayList<>();
        for(String s: parameters.keySet()) {
            nvps.add(new BasicNameValuePair(s, parameters.get(s)));
        }
        post.setEntity(new UrlEncodedFormEntity(nvps));

        StringBuilder authorizationHeader = new StringBuilder();
        authorizationHeader.append(authMethod);
        for (Map.Entry<String, String> entry : authorization.entrySet()) {
            authorizationHeader.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            authorizationHeader.append("=");
            authorizationHeader.append('"');
            authorizationHeader.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            authorizationHeader.append('"');
            if(!entry.getKey().equals(authorization.lastKey())) {
                authorizationHeader.append(", ");
            }
        }

        System.out.println("Authorization Header: " + authorizationHeader);

        post.setHeader(HttpHeaders.AUTHORIZATION, String.valueOf(authorizationHeader));
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
