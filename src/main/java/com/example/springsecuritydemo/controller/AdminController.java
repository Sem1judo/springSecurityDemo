package com.example.springsecuritydemo.controller;

import com.example.springsecuritydemo.model.Client;
import com.example.springsecuritydemo.model.Coach;
import com.example.springsecuritydemo.model.StatusCoach;
import com.example.springsecuritydemo.model.User;
import com.example.springsecuritydemo.service.impl.ClientServiceImpl;
import com.example.springsecuritydemo.service.impl.CoachServiceImpl;
import com.example.springsecuritydemo.service.impl.UserServiceImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
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

    //todo choose page where user can chose coach for himself
    //todo page with price for losing the extra weight for users - subscribes
    //todo edit all POST mappings for doing redirect and chose right page to infer

    @PostMapping("/attachCoachForUser/{id}")
    public ModelAndView attachCoachForUser(@PathVariable("id") Long clientId, HttpServletRequest request) {

        String referer = request.getHeader("Referer");
        ModelAndView mav = new ModelAndView("redirect:" + referer);

        clientService.attachCoachForUser(clientId);

        return mav;

    }

    @PostMapping("/declineCoachForUser/{id}")
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
        ModelAndView mav = new ModelAndView("admin/listCoaches");

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
        ModelAndView mav = new ModelAndView("admin/listClients");

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

        ModelAndView mav = new ModelAndView("/admin/adminPanel");

        mav.addObject("listClients", clientService.findByStatusCoach(StatusCoach.WAITING));
        return mav;
    }


}

