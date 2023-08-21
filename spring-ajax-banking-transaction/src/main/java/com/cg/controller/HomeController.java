package com.cg.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping
public class HomeController {

    @GetMapping
    public String showHome(){
        return "home";
    }

    @GetMapping("/dashboard")
    public String showDashBoard(){
        return "dashboard";
    }
}
