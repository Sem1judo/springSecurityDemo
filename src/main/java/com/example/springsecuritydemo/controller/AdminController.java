package com.example.springsecuritydemo.controller;

import com.example.springsecuritydemo.model.User;
import com.example.springsecuritydemo.service.impl.UserServiceImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


@Slf4j
@Controller
@AllArgsConstructor
@RequestMapping("/admin")
public class AdminController {
    private final UserServiceImpl userService;

    @GetMapping("/listUser")
    public ModelAndView getEvents(@RequestParam("page") Optional<Integer> page,
                                  @RequestParam("size") Optional<Integer> size) {

        ModelAndView mav = new ModelAndView("admin/listUser");

        int currentPage = page.orElse(1);
        int pageSize = size.orElse(4);

        Page<User> userPage =
                userService.findPaginated(PageRequest.of(currentPage - 1, pageSize), userService.getListUser());

        mav.addObject("userPage", userPage);

        int totalPages = userPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            mav.addObject("pageNumbers", pageNumbers);

        }
        return mav;
    }


    @GetMapping("/adminPanel")
    public ModelAndView getEvent() {

        return new ModelAndView("/admin/adminPanel");
    }

}

