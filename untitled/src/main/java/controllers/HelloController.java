package controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HelloController {
//    @GetMapping("/hello")
//    public String hello(@RequestParam("name") String name34, @RequestParam("age") String age34, Model model){
//        model.addAttribute("name",name34);
//        model.addAttribute("age",age34);
//        return "hello";
//    }
    @GetMapping("/hello/{name}/{age}")
    public String hello(@PathVariable("name") String name34,
                        @PathVariable("age") int age34, Model model){
        model.addAttribute("name",name34);
        model.addAttribute("age",age34);
        return "hello";
    }
}
