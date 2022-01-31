package com.example.springsecuritydemo.service;

import com.example.springsecuritydemo.exception.UserAlreadyExistException;
import com.example.springsecuritydemo.model.Exercise;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IExerciseService {


    Page<Exercise> findPaginated(int pageNo, Integer pageSize, String sortField, String sortDirection);

    Exercise createExercise(Exercise exercise) throws UserAlreadyExistException;

    List<Exercise> getListExercise();

    void update(Exercise exercise);

    void deleteById(long id);

    void delete(Exercise exercise);

    Exercise getById(long id);

    List<Exercise> findByKeyword(String keyword);
}
