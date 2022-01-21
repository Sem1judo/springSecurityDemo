package com.example.springsecuritydemo.controller;


import com.example.springsecuritydemo.exception.InvalidOldPasswordException;
import com.example.springsecuritydemo.exception.UserAlreadyExistException;
import com.example.springsecuritydemo.model.Client;
import com.example.springsecuritydemo.model.User;
import com.example.springsecuritydemo.model.dto.UserDto;
import com.example.springsecuritydemo.service.impl.ClientServiceImpl;
import com.example.springsecuritydemo.service.impl.UserServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
@AllArgsConstructor
@RequestMapping("/user")
public class UserController {


    private final UserServiceImpl userService;
    private final ClientServiceImpl clientService;


    @GetMapping("/profile")
    public ModelAndView getUser() {
        ModelAndView mav = new ModelAndView("/user/profile");


        Client user = clientService.getClientByEmail(getCurrentUser().getEmail());

        mav.addObject("user", user);

        return mav;
    }

    @GetMapping("/profile/{id}")
    public ModelAndView getUserProfileWithId(@PathVariable("id") long id) {
        ModelAndView mav = new ModelAndView("/user/profile");

        Client user = clientService.getByIdClient(id);
        mav.addObject("user", user);

        return mav;
    }


    @GetMapping("/billing")
    public ModelAndView getBilling() {
        ModelAndView mav = new ModelAndView("/user/billing");


        Client user = clientService.getClientByEmail(getCurrentUser().getEmail());

        mav.addObject("user", user);

        return mav;
    }


    @PostMapping(value = "/deleteUser/{id}")
    public ModelAndView deleteEvent(@PathVariable("id") long id) {

        ModelAndView mav = new ModelAndView("redirect:/" + "admin/listClients");

        userService.deleteById(id);

        return mav;
    }

    @PreAuthorize("hasAuthority('user:update')")
    @GetMapping("/editUser/{id}")
    public ModelAndView editUser(@PathVariable("id") Long clientId) {

        ModelAndView mav = new ModelAndView("user/profileEdit");

        UserDto userDto = userService.getByIdUserConvertedToUserDto(clientId);

        mav.addObject("userDto", userDto);


        return mav;
    }

    @PreAuthorize("hasAuthority('user:update')")
    @PostMapping("/updateUser/{id}")
    public ModelAndView updatingUser(@PathVariable("id") Long clientId,
                                     @Valid UserDto userDto,
                                     BindingResult bindingResult) {

        ModelAndView mav = new ModelAndView("redirect:/" + "user/profile/" + clientId);


        if (bindingResult.hasErrors()) {
            mav.setViewName("user/profileEdit");

        } else {
            try {
                userService.update(userDto);
            } catch (UserAlreadyExistException uaeEx) {
                mav.addObject("message", "An account for that username/email already exists.");
            }
        }

        return mav;
    }


    @GetMapping("/security")
    public ModelAndView getSecurity() {
        ModelAndView mav = new ModelAndView("/user/security");

        Client user = clientService.getClientByEmail(getCurrentUser().getEmail());

        mav.addObject("user", user);

        return mav;
    }

    @PostMapping("/security")
    @PreAuthorize("hasAuthority('user:update')")
    public ModelAndView changeUserPassword(
            @RequestParam("oldPassword") String oldPassword,
            @RequestParam("password") String password,
            @RequestParam("confirmPassword") String confirmPassword) {


        ModelAndView mav = new ModelAndView("user/security");

        Client user = clientService.getClientByEmail(getCurrentUser().getEmail());

        mav.addObject("user", user);

        try {
            if (!userService.checkIfValidOldPassword(user, oldPassword)) {
                throw new InvalidOldPasswordException();
            }
            if (password.equals(confirmPassword)) {
                userService.changeUserPassword(user, password);
                mav.addObject("message", "Successfully changed");
            } else mav.addObject("message", "Different passwords");
        } catch (InvalidOldPasswordException iopEx) {
            mav.addObject("message", "Incorrect current password");
        }

        return mav;
    }


    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userService.getUserByEmail(authentication.getName());
    }
}
