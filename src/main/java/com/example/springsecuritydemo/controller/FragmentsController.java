package com.example.springsecuritydemo.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/fragment")
public class FragmentsController {


    @GetMapping("/header")
    public ModelAndView headerPage() {

        return new ModelAndView("/fragment/header");
    }
}
