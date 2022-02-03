package com.example.springsecuritydemo.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

    @PostMapping("/login")
    public ModelAndView getLoginPagePost(@RequestParam(value = "error", defaultValue = "false") boolean loginError) {

        ModelAndView mav = new ModelAndView("/login");
        if (loginError) {
            mav.addObject("message", "Email or password is incorrect");
            mav.addObject("error", true);
        }
        return mav;
    }

}
