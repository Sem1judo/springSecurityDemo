package com.example.springsecuritydemo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/auth")
public class AuthController {


    @GetMapping("/login")
    public ModelAndView getLoginPageGet(@RequestParam(value = "error", defaultValue = "false") boolean loginError) {

        ModelAndView mav = new ModelAndView("user/login");
        if (loginError) {
            mav.addObject("message", "Email or password is incorrect");
            mav.addObject("error", true);
        }
        return mav;
    }

}
