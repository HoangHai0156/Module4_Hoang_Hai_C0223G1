package com.cg.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/dashboard/customers")
public class CustomerController {

    @GetMapping
    public ModelAndView showList(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("customer/list");

        return modelAndView;
    }

    @GetMapping("/info")
    public String showCustomerInfo(){
        return "customer/info";
    }

    @GetMapping("/create")
    public String showCreate(){
        return "customer/create";
    }

}
