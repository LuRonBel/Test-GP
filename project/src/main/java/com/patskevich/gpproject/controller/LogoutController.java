package com.patskevich.gpproject.controller;


import com.patskevich.gpproject.service.LogoutService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@AllArgsConstructor
@RestController
public class LogoutController {

    LogoutService logoutService;

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logoutUser (HttpServletRequest request,HttpServletResponse response) {
        return logoutService.logoutUser(request, response);
    }
}
