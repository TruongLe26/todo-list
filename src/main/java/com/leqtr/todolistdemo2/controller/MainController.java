package com.leqtr.todolistdemo2.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class MainController {

    @GetMapping("/login")
    public ModelAndView login() {
        return new ModelAndView("login");
    }

}
