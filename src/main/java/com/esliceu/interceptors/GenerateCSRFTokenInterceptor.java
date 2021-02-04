package com.esliceu.interceptors;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
public class GenerateCSRFTokenInterceptor implements HandlerInterceptor {

    @Autowired
    HttpSession session;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Cache<String, Boolean> tokenCache = (Cache<String, Boolean>) session.getAttribute("tokenCache");

        if(tokenCache == null) {
            tokenCache = CacheBuilder.newBuilder()
                    .maximumSize(5000)
                    .expireAfterWrite(1, TimeUnit.HOURS)
                    .build();
            session.setAttribute("tokenCache", tokenCache);
        }

        String token = UUID.randomUUID().toString();
        tokenCache.put(token, true);
        request.setAttribute("csrfToken", token);
        return true;
    }
}
