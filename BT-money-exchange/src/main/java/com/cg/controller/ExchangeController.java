package com.cg.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ExchangeController {

    @GetMapping
    public String showExchange(){
        return "exchange";
    }
}
