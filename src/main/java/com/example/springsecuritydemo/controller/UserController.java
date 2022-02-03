package com.example.springsecuritydemo.controller;


import com.example.springsecuritydemo.exception.InvalidOldPasswordException;
import com.example.springsecuritydemo.exception.UserAlreadyExistException;
import com.example.springsecuritydemo.model.User;
import com.example.springsecuritydemo.model.dto.UserDto;
import com.example.springsecuritydemo.service.impl.UserServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Locale;
import java.util.ResourceBundle;

@Controller
@AllArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserServiceImpl userService;

    @GetMapping("/profile")
    public ModelAndView getUser() {
        ModelAndView mav = new ModelAndView("/user/profile");

        User user = userService.getUserByEmail(getAuthCurrentEmail());

        UserDto userDto = userService.getByIdUserConvertedToUserDto(user.getId());
        mav.addObject("user", userDto);

        return mav;
    }

    @GetMapping("/profile/{id}")
    public ModelAndView getUserProfileWithId(@PathVariable("id") long id) {
        ModelAndView mav = new ModelAndView("/user/profile");

        UserDto userDto = userService.getByIdUserConvertedToUserDto(id);

        mav.addObject("user", userDto);

        return mav;
    }

    @PreAuthorize("hasAuthority('user:update')")
    @GetMapping("/editUser/{id}")
    public ModelAndView editUser(@PathVariable("id") Long userId) {

        ModelAndView mav = new ModelAndView("user/profileEdit");

        UserDto userDto = userService.getByIdUserConvertedToUserDto(userId);

        mav.addObject("userDto", userDto);




        return mav;
    }

    @PreAuthorize("hasAuthority('user:update')")
    @PostMapping("/updateUser/{id}")
    public ModelAndView updatingUser(@PathVariable("id") Long userId,
                                     @Valid UserDto userDto,
                                     BindingResult bindingResult) {

        ModelAndView mav = new ModelAndView("redirect:/" + "user/profile/" + userId);

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


    @GetMapping("/billing")
    public ModelAndView getBilling() {
        ModelAndView mav = new ModelAndView("/user/billing");

        User user = userService.getUserByEmail(getAuthCurrentEmail());


        UserDto userDto = userService.getByIdUserConvertedToUserDto(user.getId());

        mav.addObject("user", userDto);

        return mav;
    }

    @PostMapping(value = "/deleteUserByClient/{id}")
    public ModelAndView deleteUser(@PathVariable("id") long id) {
        ModelAndView mav = new ModelAndView("redirect:/" + "user/success");

        userService.deleteById(id);

        return mav;
    }

    @GetMapping("/success")
    public ModelAndView getSuccessPage() {
        return new ModelAndView("user/success");
    }

    @PostMapping(value = "/deleteUser/{id}")
    public ModelAndView deleteUserByClient(@PathVariable("id") long id) {

        ModelAndView mav = new ModelAndView("redirect:/" + "admin/listClients");


        userService.deleteById(id);

        return mav;
    }


    @GetMapping("/security")
    public ModelAndView getSecurity() {
        ModelAndView mav = new ModelAndView("/user/security");

        User user = userService.getUserByEmail(getAuthCurrentEmail());

        UserDto userDto = userService.getByIdUserConvertedToUserDto(user.getId());

        mav.addObject("user", userDto);

        return mav;
    }

    @GetMapping("/workout")
    public ModelAndView getWorkout() {
        ModelAndView mav = new ModelAndView("/user/workout");

        User user = userService.getUserByEmail(getAuthCurrentEmail());

        UserDto userDto = userService.getByIdUserConvertedToUserDto(user.getId());


        mav.addObject("user", userDto);
        mav.addObject("listExercises", userDto.getExercises());

        return mav;
    }


    @PostMapping("/security")
    @PreAuthorize("hasAuthority('user:update')")
    public ModelAndView changeUserPassword(
            @RequestParam("oldPassword") String oldPassword,
            @RequestParam("password") String password,
            @RequestParam("confirmPassword") String confirmPassword) {


        ModelAndView mav = new ModelAndView("user/security");

        User user = userService.getUserByEmail(getAuthCurrentEmail());


        mav.addObject("user", user);

        try {
            if (!userService.checkIfValidOldPassword(user, oldPassword)) {
                throw new InvalidOldPasswordException();
            }
            if (password.equals(confirmPassword)) {
                userService.changeUserPassword(user, password);
                mav.addObject("messageSuc", "Successfully changed");
            } else mav.addObject("message", "Different passwords");
        } catch (InvalidOldPasswordException iopEx) {
            mav.addObject("message", "Incorrect current password");
        }

        return mav;
    }


    private String getAuthCurrentEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }
}
