package org.example.adventurexp.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SpaController {

    @GetMapping(value = {
            "/",
            "/home",
            "/homepage",
            "/login",
            "/signup",
            "/manage-activities/**",
            "/create-activity**",
            "/reservations/**",
            "/company-events/**",
            "/dashboard/**",
            "/equipment/**",
            "/users/**"
    })
    public String forward(HttpServletRequest request) {
        String path = request.getRequestURI();

        // hvis det ligner en fil (.js, .css, .png osv.) → lad være med at fange
        if (path.contains(".") || path.startsWith("/api")) {
            return null; // lad Spring håndtere normalt
        }

        return "forward:/index.html";
    }
}
