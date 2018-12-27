package com.patskevich.gpproject.controller;

import com.patskevich.gpproject.entity.User;
import com.patskevich.gpproject.service.LogoutService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@AllArgsConstructor
@RestController
public class LogoutController {

    LogoutService logoutService;

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logoutUser (HttpServletRequest request,HttpServletResponse response) {
        //session.invalidate(); HttpSession session
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "login";
       /* HttpSession session= request.getSession(false);
        SecurityContextHolder.clearContext();
        session= request.getSession(false);
        if(session != null) {
            session.invalidate();
        }
        for(javax.servlet.http.Cookie cookie : request.getCookies()) {
            cookie.setMaxAge(0);
        }

        return "logout";*/
    }
}
