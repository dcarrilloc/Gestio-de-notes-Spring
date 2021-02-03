package com.esliceu.services;

import com.google.gson.Gson;
import org.apache.commons.codec.binary.Base64;
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
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.*;

@Service
public class TwitterOAuth2ServiceImpl implements TwitterOAuth2Service {

    @Value("${twitter-callback-uri}")
    String redirecturi;

    @Value("${twitter-consumer-key}")
    String consumerKey;

    @Value("${twitter-consumer-key-secret}")
    String consumerKeySecret;

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
    public URL getRequestToken() throws Exception {
        URL url = new URL("https://api.twitter.com/oauth/request_token");

        Timestamp timestamp = new Timestamp(System.currentTimeMillis()/1000);
        String nonce = generateNonce();
        String timestampStr = String.valueOf(timestamp.getTime());

        NavigableMap<String, String> parameters = new TreeMap<>();
        parameters.put("oauth_callback", redirecturi);
        parameters.put("oauth_consumer_key", consumerKey);
        parameters.put("oauth_nonce", nonce);
        parameters.put("oauth_signature_method", "HMAC-SHA1");
        parameters.put("oauth_timestamp", timestampStr);
        parameters.put("oauth_version", "1.0");

        String oauth_signature = buildSignature(parameters, "POST", url, "");

        NavigableMap<String, String> bodyParams = new TreeMap<>();

        NavigableMap<String, String> authorization = new TreeMap<>();
        authorization.put("oauth_callback", URLEncoder.encode(redirecturi, "UTF-8"));
        authorization.put("oauth_consumer_key", consumerKey);
        authorization.put("oauth_nonce", nonce);
        authorization.put("oauth_signature", URLEncoder.encode(oauth_signature, "UTF-8"));
        authorization.put("oauth_signature_method", "HMAC-SHA1");
        authorization.put("oauth_timestamp", timestampStr);
        authorization.put("oauth_version", "1.0");

        String[] resp = doPost(url, bodyParams, authorization, "OAuth ").split("&");
        return new URL("https://api.twitter.com/oauth/authorize?"+ resp[0]);
    }

    @Override
    public HashMap getAccessToken(String oauth_token, String oauth_verifier) throws Exception {
        URL url = new URL("https://api.twitter.com/oauth/access_token");

        Timestamp timestamp = new Timestamp(System.currentTimeMillis()/1000);
        String nonce = generateNonce();
        String timestampStr = String.valueOf(timestamp.getTime());

        NavigableMap<String, String> parameters = new TreeMap<>();
        parameters.put("oauth_consumer_key", consumerKey);
        parameters.put("oauth_nonce", nonce);
        parameters.put("oauth_signature_method", "HMAC-SHA1");
        parameters.put("oauth_timestamp", timestampStr);
        parameters.put("oauth_token", oauth_token);
        parameters.put("oauth_version", "1.0");

        String oauth_signature = buildSignature(parameters, "POST", url, "");

        NavigableMap<String, String> bodyParams = new TreeMap<>();
        bodyParams.put("oauth_verifier", oauth_verifier);

        NavigableMap<String, String> authorization = new TreeMap<>();
        authorization.put("oauth_consumer_key", consumerKey);
        authorization.put("oauth_nonce", nonce);
        authorization.put("oauth_signature", URLEncoder.encode(oauth_signature, "UTF-8"));
        authorization.put("oauth_signature_method", "HMAC-SHA1");
        authorization.put("oauth_timestamp", timestampStr);
        authorization.put("oauth_token", oauth_token);
        authorization.put("oauth_version", "1.0");

        String[] resp = doPost(url, bodyParams, authorization, "OAuth ").split("&");
        String oauth_token_resp = resp[0];
        String oauth_token_secret_resp = resp[1];

        return new Gson().fromJson(verify_credentials(oauth_token_resp.split("=")[1], oauth_token_secret_resp.split("=")[1]), HashMap.class);
    }

    private String verify_credentials(String oauth_token, String oauth_token_secret) throws Exception {
        URL verify_credentials_URL = new URL("https://api.twitter.com/1.1/account/verify_credentials.json");
        URL urlGet = new URL("https://api.twitter.com/1.1/account/verify_credentials.json?include_email=true");

        Timestamp timestamp = new Timestamp(System.currentTimeMillis()/1000);
        String nonce = generateNonce();
        String timestampStr = String.valueOf(timestamp.getTime());

        NavigableMap<String, String> signature_params = new TreeMap<>();
        signature_params.put("include_email", "true");
        signature_params.put("oauth_consumer_key", consumerKey);
        signature_params.put("oauth_nonce", nonce);
        signature_params.put("oauth_signature_method", "HMAC-SHA1");
        signature_params.put("oauth_timestamp", timestampStr);
        signature_params.put("oauth_token", oauth_token);
        signature_params.put("oauth_version", "1.0");

        String signature = buildSignature(signature_params, "GET", verify_credentials_URL, oauth_token_secret);

        NavigableMap<String, String> verify_credentials_authParams = new TreeMap<>();
        verify_credentials_authParams.put("oauth_consumer_key", consumerKey);
        verify_credentials_authParams.put("oauth_nonce", nonce);
        verify_credentials_authParams.put("oauth_signature", URLEncoder.encode(signature, "UTF-8"));
        verify_credentials_authParams.put("oauth_signature_method", "HMAC-SHA1");
        verify_credentials_authParams.put("oauth_timestamp", timestampStr);
        verify_credentials_authParams.put("oauth_token", oauth_token);
        verify_credentials_authParams.put("oauth_version", "1.0");

        return doGet(urlGet, verify_credentials_authParams, "OAuth ");
    }

    private String buildSignature(NavigableMap<String, String> params, String method, URL url, String tokenSecret) throws MalformedURLException, UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {
        StringBuilder parameterString = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            parameterString.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            parameterString.append("=");
            parameterString.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            if(!entry.getKey().equals(params.lastKey())) {
                parameterString.append("&");
            }
        }

        StringBuilder signatureBaseString = new StringBuilder();
        signatureBaseString.append(method);
        signatureBaseString.append("&");
        signatureBaseString.append(URLEncoder.encode(String.valueOf(url), "UTF-8"));
        signatureBaseString.append("&");
        signatureBaseString.append(URLEncoder.encode(String.valueOf(parameterString), "UTF-8"));

        StringBuilder signingKeysb = new StringBuilder();
        signingKeysb.append(URLEncoder.encode(consumerKeySecret, "UTF-8"));
        signingKeysb.append("&");
        signingKeysb.append(URLEncoder.encode(tokenSecret, "UTF-8"));

        return computeSignature(String.valueOf(signatureBaseString), String.valueOf(signingKeysb));
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

    public static String computeSignature(String baseString, String keyString) throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException {
        SecretKey secretKey = null;
        byte[] keyBytes = keyString.getBytes();
        secretKey = new SecretKeySpec(keyBytes, "HmacSHA1");
        Mac mac = Mac.getInstance("HmacSHA1");
        mac.init(secretKey);
        byte[] text = baseString.getBytes();
        return new String(Base64.encodeBase64(mac.doFinal(text)));
    }

    private String doPost(URL url, NavigableMap<String, String> bodyParams, NavigableMap<String, String> authorizationParams, String authMethod) throws Exception {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost post = new HttpPost(url.toString());

        if(!bodyParams.isEmpty()) {
            List<NameValuePair> nvps = new ArrayList<>();
            for(String s: bodyParams.keySet()) {
                nvps.add(new BasicNameValuePair(s, bodyParams.get(s)));
            }
            post.setEntity(new UrlEncodedFormEntity(nvps));
        }

        StringBuilder authorizationHeader = new StringBuilder();
        authorizationHeader.append(authMethod);
        for (Map.Entry<String, String> entry : authorizationParams.entrySet()) {
            authorizationHeader.append(entry.getKey());
            authorizationHeader.append("=");
            authorizationHeader.append('"');
            authorizationHeader.append(entry.getValue());
            authorizationHeader.append('"');
            if(!entry.getKey().equals(authorizationParams.lastKey())) {
                authorizationHeader.append(",");
            }
        }

        post.setHeader("Authorization", String.valueOf(authorizationHeader));
        CloseableHttpResponse response = httpClient.execute(post);
        response.getEntity();
        return EntityUtils.toString(response.getEntity());
    }

    private String doGet(URL url, NavigableMap<String, String> authorizationParams, String method) throws Exception {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet get = new HttpGet(url.toString());

        StringBuilder authorizationHeader = new StringBuilder();
        authorizationHeader.append(method);
        for (Map.Entry<String, String> entry : authorizationParams.entrySet()) {
            authorizationHeader.append(entry.getKey());
            authorizationHeader.append("=");
            authorizationHeader.append('"');
            authorizationHeader.append(entry.getValue());
            authorizationHeader.append('"');
            if(!entry.getKey().equals(authorizationParams.lastKey())) {
                authorizationHeader.append(",");
            }
        }

        get.setHeader("Authorization", String.valueOf(authorizationHeader));

        System.out.println("GET: " + Arrays.toString(get.getHeaders("Authorization")));

        CloseableHttpResponse response = httpClient.execute(get);
        response.getEntity();
        return EntityUtils.toString(response.getEntity());
    }
}
