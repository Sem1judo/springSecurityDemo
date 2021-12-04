package com.example.springsecuritydemo.controller;

import com.example.springsecuritydemo.model.Client;
import com.example.springsecuritydemo.model.Coach;
import com.example.springsecuritydemo.service.impl.ClientServiceImpl;
import com.example.springsecuritydemo.service.impl.CoachServiceImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

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

    @PreAuthorize("hasAuthority('admin:read')")
    @GetMapping("/listCoaches")
    public ModelAndView findPaginatedCoaches(@RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
                                             @RequestParam("size") Optional<Integer> size,
                                             @RequestParam(value = "sort", defaultValue = "firstName") String sortField,
                                             @RequestParam(value = "sortDir", defaultValue = "asc") String sortDir
    ) {
        ModelAndView mav = new ModelAndView("admin/listCoaches");

        int pageSize = size.orElse(10);

        Page<Coach> page = coachService.findPaginated(pageNo, pageSize, sortField, sortDir);
        List<Coach> listCoaches = page.getContent();

        getPagingAndSortingParams(pageNo, sortField, sortDir, mav, page.getTotalPages(), page.getTotalElements());

        mav.addObject("listCoaches", listCoaches);

        return mav;
    }


    @PreAuthorize("hasAuthority('admin:read')")
    @GetMapping("/listClients")
    public ModelAndView findPaginatedClients(@RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
                                             @RequestParam("size") Optional<Integer> size,
                                             @RequestParam(value = "sort", defaultValue = "weight") String sortField,
                                             @RequestParam(value = "sortDir", defaultValue = "asc") String sortDir
    ) {
        ModelAndView mav = new ModelAndView("admin/listClients");

        int pageSize = size.orElse(10);

        Page<Client> page = clientService.findPaginated(pageNo, pageSize, sortField, sortDir);
        List<Client> listClients = page.getContent();

        getPagingAndSortingParams(pageNo, sortField, sortDir, mav, page.getTotalPages(), page.getTotalElements());

        mav.addObject("listClients", listClients);

        return mav;
    }

    private void getPagingAndSortingParams(int pageNo,
                                           String sortField,
                                           String sortDir,
                                           ModelAndView mav, int totalPages, long totalElements) {
        mav.addObject("currentPage", pageNo);
        mav.addObject("totalPages", totalPages);
        mav.addObject("totalItems", totalElements);

        mav.addObject("sortField", sortField);
        mav.addObject("sortDir", sortDir);
        mav.addObject("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
    }


    @PreAuthorize("hasAuthority('admin:read')")
    @GetMapping("/adminPanel")
    public ModelAndView getAdminPanel() {

        return new ModelAndView("/admin/adminPanel");
    }
}

