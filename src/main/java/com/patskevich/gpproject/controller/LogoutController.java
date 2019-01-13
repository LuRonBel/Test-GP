package com.patskevich.gpproject.controller;

import com.patskevich.gpproject.service.LogoutService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@AllArgsConstructor
@RestController
public class LogoutController {

    private static final String CURRENT_PAGE_URL = "/logout";
    private final LogoutService logoutService;

    @PostMapping(LogoutController.CURRENT_PAGE_URL)
    public String logoutUser (final HttpServletRequest request, final HttpServletResponse response) {
        return logoutService.logoutUser(request, response);
    }
}
