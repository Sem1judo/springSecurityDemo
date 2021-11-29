package com.example.springsecuritydemo.controller;

import com.example.springsecuritydemo.model.Client;
import com.example.springsecuritydemo.model.User;
import com.example.springsecuritydemo.service.impl.ClientServiceImpl;
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
@RequestMapping("/client")
public class ClientController {


    private static final String REDIRECT = "redirect:";
    private final ClientServiceImpl clientService;


    @GetMapping("/addClient")
    ModelAndView addingPage() {
        ModelAndView mav = new ModelAndView("client/addClient");

        mav.addObject("client", new Client());
        mav.addObject("user", new User());
        return mav;
    }

    @PostMapping("/addClient")
    ModelAndView adding(@ModelAttribute @Valid Client client , BindingResult bindingResult) {

        ModelAndView mav = new ModelAndView();

        if (bindingResult.hasErrors()) {
            mav.setViewName("client/addClient");
        } else {
            clientService.addClientInfo(client, getAuthCurrentEmail());
            mav.setViewName(REDIRECT + "/user/profile");
        }
        return mav;
    }

    private String getAuthCurrentEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

}

