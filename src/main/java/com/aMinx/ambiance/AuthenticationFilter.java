package com.aMinx.ambiance;

import com.aMinx.ambiance.controllers.service.UtilitiesService;
import com.aMinx.ambiance.data.UserDAO;
import com.aMinx.ambiance.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class AuthenticationFilter extends HandlerInterceptorAdapter {

    @Autowired
    UserDAO userRepository;

    @Autowired
    UtilitiesService utilitiesService;

    private static final List<String> whitelist = Arrays.asList("/login", "/register","/generalStyles.css" ,"/images/AmbianceLogo.png", "/", "/logout", "/css");

    private static boolean isWhitelisted(String path) {
        for (String pathRoot : whitelist) {
            if (path.startsWith(pathRoot)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws IOException {

        if (isWhitelisted(request.getRequestURI())) {
            // returning true indicates that the request may proceed
            return true;
        }

        HttpSession session = request.getSession();
        User user = utilitiesService.getUserFromSession();

        // The user is logged in
        if (user != null) {
            return true;
        }

        // The user is NOT logged in
        response.sendRedirect("/login");

        return false;
    }

}