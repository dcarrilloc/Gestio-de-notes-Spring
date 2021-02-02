package com.esliceu.interceptors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Component
public class SessionInterceptor implements HandlerInterceptor {

    @Autowired
    HttpSession session;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Long userid = (Long) session.getAttribute("userid");

        if (userid == null) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.sendRedirect("/");
            return false;
        }

        return true;
    }

}
