package com.balukonis.app.universitymicroserviceUI.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/")
    public String homepage() {
        return "ui/home1";
    }


    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/information")
    public String aboutUs(){
        return "aboutus";
    }
}

