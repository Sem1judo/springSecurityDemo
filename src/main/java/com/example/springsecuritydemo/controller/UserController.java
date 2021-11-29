package com.example.springsecuritydemo.controller;


import com.example.springsecuritydemo.model.User;
import com.example.springsecuritydemo.service.impl.UserServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@AllArgsConstructor
@RequestMapping("/user")
public class UserController {


    private final UserServiceImpl userService;

    @GetMapping("/profile")
    public ModelAndView getEvent() {
        ModelAndView mav = new ModelAndView("/user/profile");

        User user = getCurrentUser();

        mav.addObject("user", user);

        return mav;
    }

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userService.getUserByEmail(authentication.getName());
    }
}
