package com.patskevich.gpproject.service;

import com.patskevich.gpproject.configuration.LanguageMessage;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
public class LogoutService {

    public String logoutUser (final HttpServletRequest request,final HttpServletResponse response) {
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getName().equals("nonymousUser")) return LanguageMessage.getText("no.authorized");
            else {
                new SecurityContextLogoutHandler().logout(request, response, auth);
                return LanguageMessage.getText("you.logged.out");
        }
    }
}
