package com.example.springsecuritydemo.controller;

import com.example.springsecuritydemo.exception.NoSuchEntityException;
import com.example.springsecuritydemo.exception.UserAlreadyExistException;
import com.example.springsecuritydemo.model.Client;
import com.example.springsecuritydemo.model.dto.UserDto;
import com.example.springsecuritydemo.service.impl.ClientServiceImpl;
import com.example.springsecuritydemo.service.impl.CoachServiceImpl;
import com.example.springsecuritydemo.service.impl.ExerciseServiceImpl;
import com.example.springsecuritydemo.service.impl.UserServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
@AllArgsConstructor
@RequestMapping("/client")
public class ClientController {


    private static final String REDIRECT = "redirect:";
    private final ClientServiceImpl clientService;
    private final UserServiceImpl userService;
    private final CoachServiceImpl coachService;
    private final ExerciseServiceImpl exerciseService;


    @PostMapping("/addCoachForUser/{id}")
    public ModelAndView addCoachForUser(@PathVariable("id") Long coachId, HttpServletRequest request) {

        String referer = request.getHeader("Referer");
        ModelAndView mav = new ModelAndView("redirect:" + referer);

        clientService.addCoachForUser(coachId, getAuthCurrentEmail());

        return mav;

    }

    @PostMapping("/deleteCoachForClient/{id}")
    public ModelAndView deleteCoachForUser(@PathVariable("id") Long coachId, HttpServletRequest request) {

        String referer = request.getHeader("Referer");
        ModelAndView mav = new ModelAndView("redirect:" + referer);

        clientService.deleteCoachForUser(getAuthCurrentEmail(), coachId);

        return mav;

    }

    @PostMapping("/addExercisesForClient/{id}")
    public ModelAndView addExerciseForUser(@PathVariable("id") Long clientId,
                                           UserDto client) {

        ModelAndView mav = new ModelAndView("redirect:" + "/client/listExercisesForClient/" + clientId);


        userService.update(client);

        return mav;

    }


    @GetMapping("/listCoachesForClient")
    public ModelAndView findPaginatedCoaches(@RequestParam(required = false) String keyword) {
        ModelAndView mav = new ModelAndView("client/listCoachesForClient");


        if (keyword != null && keyword.length() >= 1) {
            mav.addObject("listCoaches", coachService.findByKeyword(keyword));
            mav.addObject("keyword", keyword);
        } else {
            mav.addObject("listCoaches", coachService.getListCoach());
        }


        try {
            mav.addObject("client", clientService.getClientByEmail(getAuthCurrentEmail()));
        } catch (NoSuchEntityException e) {
            mav.addObject("client", null);
        }


        return mav;
    }


    @GetMapping("/listExercisesForClient/{id}")
    public ModelAndView findPaginatedCoaches(@PathVariable("id") Long clientId,
                                             @RequestParam(required = false) String keyword) {
        ModelAndView mav = new ModelAndView("client/listExercisesForClient");

        UserDto userDto = userService.getByIdUserConvertedToUserDto(clientId);
        Client client = clientService.getByIdClient(clientId);

        mav.addObject("client", userDto);
        mav.addObject("clientEntity", client);

        if (keyword != null && keyword.length() >= 1) {
            mav.addObject("listExercises", exerciseService.findByKeyword(keyword));
            mav.addObject("keyword", keyword);
        } else {
            mav.addObject("listExercises", exerciseService.getListExercise());
        }


        return mav;
    }


    @PreAuthorize("hasAuthority('admin:update')")
    @GetMapping("/editClient/{id}")
    public ModelAndView editPage(@PathVariable("id") Long clientId) {

        ModelAndView mav = new ModelAndView("user/profile");

        UserDto userDto = userService.getByIdUserConvertedToUserDto(clientId);

        mav.addObject("userDto", userDto);

        return mav;
    }

    @PreAuthorize("hasAuthority('admin:update')")
    @PostMapping("/updateClient/{id}")
    public ModelAndView updating(@PathVariable("id") Long clientId,
                                 @Valid UserDto userDto,
                                 BindingResult bindingResult) {

        ModelAndView mav = new ModelAndView("redirect:/" + "user/profile");

        if (bindingResult.hasErrors()) {

            mav.setViewName("user/profile");
        } else {
            try {
                userService.update(userDto);
            } catch (UserAlreadyExistException uaeEx) {
                mav.addObject("message", "An account for that username/email already exists.");
            }
        }

        return mav;
    }


    private String getAuthCurrentEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

}

