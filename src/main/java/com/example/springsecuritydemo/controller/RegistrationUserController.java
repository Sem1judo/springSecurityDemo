package com.example.springsecuritydemo.controller;


import com.example.springsecuritydemo.exception.UserAlreadyExistException;
import com.example.springsecuritydemo.model.dto.UserDto;
import com.example.springsecuritydemo.service.impl.UserServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;


@Controller
@AllArgsConstructor
@RequestMapping("/user")
public class RegistrationUserController {


    private static final String REDIRECT = "redirect:";
    private final UserServiceImpl userService;


    @GetMapping("/registration")
    ModelAndView registrationPage() {
        ModelAndView mav = new ModelAndView("user/registration");


        mav.addObject("userDto", new UserDto());
        mav.addObject("isClient", true);
        return mav;
    }

    @PostMapping("/registration")
    ModelAndView registration(@ModelAttribute @Valid UserDto userDto, BindingResult bindingResult) {

        ModelAndView mav = new ModelAndView();

        if (bindingResult.hasErrors()) {
            mav.setViewName("user/registration");
        } else {
            try {
                userService.registerNewUser(userDto);
                mav.setViewName(REDIRECT);
            } catch (UserAlreadyExistException uaeEx) {
                mav.addObject("message", "An account for that username/email already exists.");
            }
        }
        return mav;
    }
}
