package com.example.springsecuritydemo.controller;


import com.example.springsecuritydemo.exception.UserAlreadyExistException;
import com.example.springsecuritydemo.model.dto.UserDto;
import com.example.springsecuritydemo.service.impl.UserServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;


@Controller
@AllArgsConstructor
@RequestMapping("/user")
public class RegistrationUserController {


    private static final String REDIRECT = "redirect:";
    private final UserServiceImpl userService;
    private final JavaMailSender mailSender;


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
                mav.setViewName(REDIRECT + "profile");
            } catch (UserAlreadyExistException uaeEx) {
                mav.addObject("message", "An account for that username/email already exists.");
            }
        }
        return mav;
    }

    @GetMapping("/signUpEmail")
    ModelAndView signUpEmail(HttpServletRequest request, @RequestParam(value = "plan", defaultValue = "empty") String plan, @RequestParam(value = "comment", defaultValue = "empty") String comment) {
        ModelAndView mav = new ModelAndView("/index");

        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");


        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("noreply@smart-sport.com");
        message.setTo("respect.dun@gmail.com");
        message.setSubject("User data: ");
        message.setText("Full name :" + name + ", Email :" + email + ", Phone: " + phone + ",Plan: " + plan + ", User Comment = " + comment);
        mailSender.send(message);


        return mav;
    }
}
