package com.example.springsecuritydemo.controller;


import com.example.springsecuritydemo.exception.UserAlreadyExistException;
import com.example.springsecuritydemo.model.Client;
import com.example.springsecuritydemo.model.Exercise;
import com.example.springsecuritydemo.model.StatusCoach;
import com.example.springsecuritydemo.model.dto.UserDto;
import com.example.springsecuritydemo.service.IExerciseService;
import com.example.springsecuritydemo.service.impl.ExerciseServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@AllArgsConstructor
@RequestMapping("/exercise")
public class ExerciseController {

    private ExerciseServiceImpl exerciseService;

    @PreAuthorize("hasAuthority('admin:read')")
        @GetMapping("/listExercises")
    public ModelAndView getListExercises(@RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
                                         @RequestParam("size")
                                                 Optional<Integer> size,
                                         @RequestParam(value = "sortField", defaultValue = "id") String sortField,
                                         @RequestParam(value = "sortDir", defaultValue = "asc") String sortDir
    ) {
        ModelAndView mav = new ModelAndView("/admin/adminPanelExercises");

        int pageSize = size.orElse(10);

        Page<Exercise> page = exerciseService.findPaginated(pageNo, pageSize, sortField, sortDir);
        List<Exercise> listExercises = page.getContent();


        mav.addObject("currentPage", pageNo);
        mav.addObject("totalPages", page.getTotalPages());
        mav.addObject("totalItems", page.getTotalElements());

        mav.addObject("sortField", sortField);
        mav.addObject("sortDir", sortDir);
        mav.addObject("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

        mav.addObject("listExercises", listExercises);

        return mav;

    }

    @GetMapping("/createExercise")
    @PreAuthorize("hasAuthority('admin:create')")
    public ModelAndView createExercisePage(@Valid Exercise exercise) {

        ModelAndView mav = new ModelAndView("/admin/adminPanelAddExercise");

        mav.addObject("exercise", new Exercise());

        return mav;
    }

    @PostMapping("/createExercise")
    @PreAuthorize("hasAuthority('admin:create')")
    public ModelAndView createExercise(@ModelAttribute @Valid Exercise exercise, BindingResult bindingResult) {
        ModelAndView mav = new ModelAndView();

        if (bindingResult.hasErrors()) {
            mav.setViewName("/admin/adminPanelAddExercise");
        } else {
            exerciseService.createExercise(exercise);
            mav.setViewName("redirect:/" + "exercise/listExercises");
        }
        return mav;
    }

    @PostMapping(value = "/deleteExercise/{id}")
    @PreAuthorize("hasAuthority('admin:delete')")
    public ModelAndView deleteExercise(@PathVariable("id") long id) {

        ModelAndView mav = new ModelAndView("redirect:/" + "exercise/listExercises");

        exerciseService.deleteById(id);

        return mav;
    }

    @PreAuthorize("hasAuthority('admin:update')")
    @GetMapping("/editExercise/{id}")
    public ModelAndView editPage(@PathVariable("id") Long exerciseId) {

        ModelAndView mav = new ModelAndView("admin/adminPanelEditExercise");

        Exercise exercise = exerciseService.getById(exerciseId);

        mav.addObject("exercise", exercise);

        return mav;
    }

    @PreAuthorize("hasAuthority('admin:update')")
    @PostMapping("/updateExercise/{id}")
    public ModelAndView updating(@PathVariable("id") Long exerciseId,
                                 @Valid Exercise exercise,
                                 BindingResult bindingResult) {

        ModelAndView mav = new ModelAndView("redirect:/" + "exercise/listExercises");

        if (bindingResult.hasErrors()) {
            mav.setViewName("admin/adminPanelEditExercise");

        } else {
            exerciseService.update(exercise);
        }

        return mav;
    }
}
