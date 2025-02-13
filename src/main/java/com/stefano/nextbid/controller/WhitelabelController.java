package com.stefano.nextbid.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class WhitelabelController implements ErrorController {

    @RequestMapping("/error")
    public void redirect(HttpServletResponse response) throws IOException {
        response.sendRedirect("/");
    }
}
