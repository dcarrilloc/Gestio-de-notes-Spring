package com.esliceu.interceptors;

import com.google.common.cache.Cache;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Component
public class CheckCSRFTokenInterceptor implements HandlerInterceptor {

    @Autowired
    HttpSession session;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(request.getMethod().equalsIgnoreCase("POST")) {
            String tokenFromRequest = request.getParameter("_csrftoken");
            Cache<String, Boolean> tokenCache = (Cache<String, Boolean>) session.getAttribute("tokenCache");
            if(tokenCache == null || tokenCache.getIfPresent(tokenFromRequest) == null) {
                response.setStatus(HttpStatus.SC_FORBIDDEN);
                return false;
            }
        }
        return true;
    }

}
