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
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@AllArgsConstructor
@RequestMapping("/client")
public class ClientController {


    private static final String REDIRECT = "redirect:";
    private final ClientServiceImpl clientService;
    private final UserServiceImpl userService;
    private final CoachServiceImpl coachService;


    @PostMapping("/addCoachForUser/{id}")
    public ModelAndView addCoachForUser(@PathVariable("id") Long coachId, HttpServletRequest request) {

        String referer = request.getHeader("Referer");
        ModelAndView mav = new ModelAndView("redirect:" + referer);

        clientService.addCoachForUser(coachId, getAuthCurrentEmail());

        return mav;

    }

    @PostMapping("/deleteCoachForUser/{id}")
    public ModelAndView deleteCoachForUser(@PathVariable("id") Long coachId, HttpServletRequest request) {

        String referer = request.getHeader("Referer");
        ModelAndView mav = new ModelAndView("redirect:" + referer);

        clientService.deleteCoachForUser(getAuthCurrentEmail(), coachId);

        return mav;

    }




    @GetMapping("/listCoachesForClient")
    public ModelAndView findPaginatedCoaches(@RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
                                             @RequestParam("size") Optional<Integer> size,
                                             @RequestParam(value = "sort", defaultValue = "firstName") String sortField,
                                             @RequestParam(value = "sortDir", defaultValue = "asc") String sortDir
    ) {
        ModelAndView mav = new ModelAndView("client/listCoachesForClient");

        int pageSize = size.orElse(10);

        Page<Coach> page = coachService.findPaginated(pageNo, pageSize, sortField, sortDir);
        List<Coach> listCoaches = page.getContent();

        mav.addObject("client", clientService.getClientByEmail(getAuthCurrentEmail()));
        mav.addObject("currentPage", pageNo);
        mav.addObject("totalPages", page.getTotalPages());
        mav.addObject("totalItems", page.getTotalElements());

        mav.addObject("sortField", sortField);
        mav.addObject("sortDir", sortDir);
        mav.addObject("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

        mav.addObject("listCoaches", listCoaches);

        return mav;
    }


    @GetMapping("/viewClient/{id}")
    public ModelAndView viewClient(@PathVariable("id") Long clientId) {
        ModelAndView mav = new ModelAndView("client/profileClient");


        mav.addObject("client", clientService.getByIdClient(clientId));

        return mav;
    }

    @GetMapping("/addClient")
    ModelAndView addingPage() {
        ModelAndView mav = new ModelAndView("client/addClient");

        mav.addObject("client", new Client());
        mav.addObject("user", new User());
        return mav;
    }

    @PostMapping("/addClient")
    ModelAndView adding(@ModelAttribute @Valid Client client, BindingResult bindingResult) {

        ModelAndView mav = new ModelAndView();

        if (bindingResult.hasErrors()) {
            mav.setViewName("client/addClient");
        } else {
            clientService.addClientInfo(client, getAuthCurrentEmail());
            mav.setViewName(REDIRECT + "/user/profile");
        }
        return mav;
    }


    @PreAuthorize("hasAuthority('admin:update')")
    @GetMapping("/editClient/{id}")
    public ModelAndView editPage(@PathVariable("id") Long clientId) {

        ModelAndView mav = new ModelAndView("client/editFormClient");

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

            mav.setViewName("client/editFormClient");
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

