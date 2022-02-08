package com.example.springsecuritydemo.controller;

import com.example.springsecuritydemo.service.impl.CoachServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@AllArgsConstructor
@RequestMapping("/coach")
public class CoachController {

    private final CoachServiceImpl coachService;

    @GetMapping("/viewCoach/{id}")
    @PreAuthorize("hasAuthority('admin:read')")
    public ModelAndView viewCoach(@PathVariable("id") Long coachId) {
        ModelAndView mav = new ModelAndView("coach/profileCoach");

        mav.addObject("coach", coachService.getByIdCoach(coachId));

        return mav;
    }

}

