package com.example.demo.interceptor;

import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UriInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String urlList[] = {
                "^/api/v1/users-id$",
                "^/api/v1/users(/[^//]*){0,1}$",
                "^/api/v1/user/[^//]+$"
        };

        String uri = request.getRequestURI();
        for (String urlPattern : urlList) {
            if (uri.matches(urlPattern)) {
                return true;
            }
        }
        response.setStatus(HttpStatus.NOT_FOUND.value());
        response.getWriter().write("<html><head><title>Error Page</title></head><body>Invalid Request</body></html>");
        return false;
    }
}
