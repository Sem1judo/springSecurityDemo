package com.example.springsecuritydemo.controller;

import com.example.springsecuritydemo.exception.UserAlreadyExistException;
import com.example.springsecuritydemo.model.Client;
import com.example.springsecuritydemo.model.Coach;
import com.example.springsecuritydemo.model.User;
import com.example.springsecuritydemo.model.dto.UserDto;
import com.example.springsecuritydemo.service.impl.ClientServiceImpl;
import com.example.springsecuritydemo.service.impl.CoachServiceImpl;
import com.example.springsecuritydemo.service.impl.UserServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
@AllArgsConstructor
@RequestMapping("/coach")
public class CoachController {


    private static final String REDIRECT = "redirect:";
    private final CoachServiceImpl coachService;



    @GetMapping("/addCoach")
    ModelAndView addingPage() {
        ModelAndView mav = new ModelAndView("coach/addCoach");

        mav.addObject("coach", new Coach());
        return mav;
    }

    @PostMapping("/addCoach")
    ModelAndView adding(@ModelAttribute @Valid Coach coach , BindingResult bindingResult) {

        ModelAndView mav = new ModelAndView();

        if (bindingResult.hasErrors()) {
            mav.setViewName("coach/addCoach");
        } else {
            coachService.addCoachInfo(coach, getAuthCurrentEmail());
            mav.setViewName(REDIRECT + "/user/profile");
        }
        return mav;
    }
    private String getAuthCurrentEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

}

