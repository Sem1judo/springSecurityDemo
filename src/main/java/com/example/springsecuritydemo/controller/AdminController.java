package com.example.springsecuritydemo.controller;

import com.example.springsecuritydemo.exception.UserAlreadyExistException;
import com.example.springsecuritydemo.model.Client;
import com.example.springsecuritydemo.model.Coach;
import com.example.springsecuritydemo.model.StatusCoach;
import com.example.springsecuritydemo.model.User;
import com.example.springsecuritydemo.model.dto.TypeUser;
import com.example.springsecuritydemo.model.dto.UserDto;
import com.example.springsecuritydemo.service.impl.ClientServiceImpl;
import com.example.springsecuritydemo.service.impl.CoachServiceImpl;
import com.example.springsecuritydemo.service.impl.ExerciseServiceImpl;
import com.example.springsecuritydemo.service.impl.UserServiceImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Slf4j
@Controller
@AllArgsConstructor
@RequestMapping("/admin")
public class AdminController {
    private final CoachServiceImpl coachService;
    private final ClientServiceImpl clientService;
    private final ExerciseServiceImpl exerciseService;
    private static final String REDIRECT = "redirect:";
    private final UserServiceImpl userService;


    @PostMapping("/attachCoachForUser/{id}")
    @PreAuthorize("hasAuthority('admin:create')")
    public ModelAndView attachCoachForUser(@PathVariable("id") Long clientId, HttpServletRequest request) {

        String referer = request.getHeader("Referer");
        ModelAndView mav = new ModelAndView(REDIRECT + referer);

        clientService.attachCoachForUser(clientId);

        return mav;

    }

    @PostMapping("/declineCoachForUser/{id}")
    @PreAuthorize("hasAuthority('admin:delete')")
    public ModelAndView declineCoachForUser(@PathVariable("id") Long clientId, HttpServletRequest request) {

        String referer = request.getHeader("Referer");
        ModelAndView mav = new ModelAndView("redirect:" + referer);

        clientService.declineCoachForUser(clientId);

        return mav;

    }

    @PreAuthorize("hasAuthority('admin:read')")
    @GetMapping("/listCoaches")
    public ModelAndView findPaginatedCoaches(@RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
                                             @RequestParam("size") Optional<Integer> size,
                                             @RequestParam(value = "sortField", defaultValue = "firstName") String sortField,
                                             @RequestParam(value = "sortDir", defaultValue = "asc") String sortDir
    ) {
        ModelAndView mav = new ModelAndView("admin/adminPanelCoaches");

        int pageSize = size.orElse(10);

        Page<Coach> page = coachService.findPaginated(pageNo, pageSize, sortField, sortDir);
        List<Coach> listCoaches = page.getContent();

        mav.addObject("currentPage", pageNo);
        mav.addObject("totalPages", page.getTotalPages());
        mav.addObject("totalItems", page.getTotalElements());

        mav.addObject("sortField", sortField);
        mav.addObject("sortDir", sortDir);
        mav.addObject("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

        mav.addObject("listCoaches", listCoaches);

        return mav;
    }


    @PreAuthorize("hasAuthority('admin:read')")
    @GetMapping("/listClients")
    public ModelAndView findPaginatedClients(@RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
                                             @RequestParam("size") Optional<Integer> size,
                                             @RequestParam(value = "sortField", defaultValue = "id") String sortField,
                                             @RequestParam(value = "sortDir", defaultValue = "asc") String sortDir
    ) {
        ModelAndView mav = new ModelAndView("admin/adminPanelClients");

        int pageSize = size.orElse(10);

        Page<Client> page = clientService.findPaginated(pageNo, pageSize, sortField, sortDir);
        List<Client> listClients = page.getContent();


        mav.addObject("currentPage", pageNo);
        mav.addObject("totalPages", page.getTotalPages());
        mav.addObject("totalItems", page.getTotalElements());

        mav.addObject("sortField", sortField);
        mav.addObject("sortDir", sortDir);
        mav.addObject("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

        mav.addObject("listClients", listClients);

        return mav;
    }


    @PreAuthorize("hasAuthority('admin:read')")
    @GetMapping("/adminPanel")
    public ModelAndView getAdminPanel() {

        ModelAndView mav = new ModelAndView("admin/adminPanel");

        mav.addObject("listClients", clientService.findByStatusCoach(StatusCoach.WAITING));

        mav.addObject("totalNumberCoaches", coachService.getListCoach().size());
        mav.addObject("totalNumberClients", clientService.getListClient().size());
        mav.addObject("totalNumberExercises", exerciseService.getListExercise().size());
        return mav;
    }

    @GetMapping("/registerUser")
    @PreAuthorize("hasAuthority('admin:create')")
    ModelAndView registrationPage() {
        ModelAndView mav = new ModelAndView("admin/adminPanelAddUser");


        mav.addObject("userDto", new UserDto());
        mav.addObject("isClient", true);
        return mav;
    }

    @PostMapping("/registerUser")
    @PreAuthorize("hasAuthority('admin:create')")
    ModelAndView registration(@ModelAttribute @Valid UserDto userDto, BindingResult bindingResult) {

        ModelAndView mav = new ModelAndView();

        if (bindingResult.hasErrors()) {
            mav.setViewName("admin/adminPanelAddUser");
        } else {
            try {
                userService.registerNewUser(userDto);
                if (userDto.getTypeUser().equals(TypeUser.CLIENT)) {
                    mav.setViewName(REDIRECT + "/admin/listClients");
                }
                if (userDto.getTypeUser().equals(TypeUser.COACH)) {
                    mav.setViewName(REDIRECT + "/admin/listCoaches");
                }
            } catch (UserAlreadyExistException uaeEx) {
                mav.setViewName("admin/adminPanelAddUser");
                mav.addObject("message", "An account for that username/email already exists.");
            }
        }


        return mav;
    }

    @PreAuthorize("hasAuthority('admin:update')")
    @GetMapping("/editClient/{id}")
    public ModelAndView editClientPage(@PathVariable("id") Long clientId) {

        ModelAndView mav = new ModelAndView("admin/adminPanelEditClient");

        UserDto userDto = userService.getByIdUserConvertedToUserDto(clientId);

        mav.addObject("userDto", userDto);

        return mav;
    }

    @PreAuthorize("hasAuthority('admin:update')")
    @PostMapping("/updateClient/{id}")
    public ModelAndView updatingClient(@PathVariable("id") Long clientId,
                                 @Valid UserDto userDto,
                                 BindingResult bindingResult) {

        ModelAndView mav = new ModelAndView("redirect:/" + "admin/listClients");

        if (bindingResult.hasErrors()) {
            mav.setViewName("admin/adminPanelEditClient");
        } else {
            try {
                userService.update(userDto);
            } catch (UserAlreadyExistException uaeEx) {
                mav.addObject("message", "An account for that username/email already exists.");
            }
        }

        return mav;
    }

    @PreAuthorize("hasAuthority('admin:update')")
    @GetMapping("/editCoach/{id}")
    public ModelAndView editCoachPage(@PathVariable("id") Long coachId) {

        ModelAndView mav = new ModelAndView("admin/adminPanelEditCoach");

        UserDto userDto = userService.getByIdUserConvertedToUserDto(coachId);

        mav.addObject("userDto", userDto);

        return mav;
    }

    @PreAuthorize("hasAuthority('admin:update')")
    @PostMapping("/updateCoach/{id}")
    public ModelAndView updatingCoach(@PathVariable("id") Long coachId,
                                 @Valid UserDto userDto,
                                 BindingResult bindingResult) {

        ModelAndView mav = new ModelAndView("redirect:/" + "admin/listCoaches");

        System.out.println(userDto);

        if (bindingResult.hasErrors()) {
            mav.setViewName("admin/adminPanelEditCoach");
        } else {
            try {
                userService.update(userDto);
            } catch (UserAlreadyExistException uaeEx) {
                mav.addObject("message", "An account for that username/email already exists.");
            }
        }

        return mav;
    }

}

