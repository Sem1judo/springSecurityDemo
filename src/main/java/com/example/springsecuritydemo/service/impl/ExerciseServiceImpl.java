package com.example.springsecuritydemo.service.impl;

import com.example.springsecuritydemo.exception.NoSuchEntityException;
import com.example.springsecuritydemo.exception.ServiceException;
import com.example.springsecuritydemo.exception.UserAlreadyExistException;
import com.example.springsecuritydemo.model.*;
import com.example.springsecuritydemo.model.dto.TypeUser;
import com.example.springsecuritydemo.model.dto.UserDto;
import com.example.springsecuritydemo.repository.ClientRepository;
import com.example.springsecuritydemo.repository.ExerciseRepository;
import com.example.springsecuritydemo.service.IClientService;
import com.example.springsecuritydemo.service.IExerciseService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class ExerciseServiceImpl implements IExerciseService {
    private ExerciseRepository exerciseRepository;


    private static final String MISSING_ID_ERROR_MESSAGE = "Missing id exercise";
    private static final String NOT_EXIST_ENTITY = "Doesn't exist such exercise";


    @Override
    public Page<Exercise> findPaginated(int pageNo, Integer pageSize, String sortField, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
                Sort.by(sortField).descending();

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        return exerciseRepository.findAll(pageable);

    }

    @Override
    public Exercise createExercise(Exercise exercise) throws UserAlreadyExistException {
        log.debug("Trying to add exercise: {}", exercise);

        try {
            return exerciseRepository.save(exercise);
        } catch (DataAccessException e) {
            log.error("Failed to add exercise: {}", exercise, e);
            throw new ServiceException("Failed to add exercise", e);
        }
    }


    @Override
    public List<Exercise> getListExercise() {
        log.debug("Trying to get list of exercises");
        try {
            return (List<Exercise>) exerciseRepository.findAll();
        } catch (EmptyResultDataAccessException e) {
            log.warn("Exercises is not exist");
            throw new NoSuchEntityException("Doesn't exist such exercises");
        } catch (DataAccessException e) {
            log.error("Failed to get list of exercises", e);
            throw new ServiceException("Failed to get list of exercises", e);
        }
    }


    @Override
    public void update(Exercise exercise) {
        log.debug("Trying to update exercise: {}", exercise);

        if (exercise.getId() == 0) {
            log.warn(MISSING_ID_ERROR_MESSAGE);
            throw new ServiceException(MISSING_ID_ERROR_MESSAGE);
        }

        try {
            exerciseRepository.findById(exercise.getId());
        } catch (EmptyResultDataAccessException e) {
            log.warn("Not existing exercise: {}", exercise);
            throw new NoSuchEntityException(NOT_EXIST_ENTITY);
        } catch (DataAccessException e) {
            log.error("Failed to retrieve exercise: {}", exercise);
            throw new ServiceException("Failed to retrieve exercise: ", e);
        }
        try {
            exerciseRepository.save(exercise);
        } catch (DataAccessException e) {
            log.error("Failed to update exercise: {}", exercise);
            throw new ServiceException("Problem with updating exercise");
        }
    }



    @Override
    public void deleteById(long id) {
        log.debug("Trying to delete exercise with id={}", id);
        if (id == 0) {
            log.warn(MISSING_ID_ERROR_MESSAGE);
            throw new ServiceException(MISSING_ID_ERROR_MESSAGE);
        }
        try {
            exerciseRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            log.warn("Not existing exercise with id={}", id);
            throw new NoSuchEntityException(NOT_EXIST_ENTITY);
        } catch (DataAccessException e) {
            log.error("failed to delete exercise with id={}", id, e);
            throw new ServiceException("Failed to delete exercise by id", e);
        }
    }


    @Override
    public void delete(Exercise exercise) {
        log.debug("Trying to delete exercise = {}", exercise);

        if (exercise == null) {
            log.warn(MISSING_ID_ERROR_MESSAGE);
            throw new ServiceException(MISSING_ID_ERROR_MESSAGE);
        }
        try {
            exerciseRepository.delete(exercise);
        } catch (EmptyResultDataAccessException e) {
            log.warn("Not existing exercise = {}", exercise);
            throw new NoSuchEntityException(NOT_EXIST_ENTITY);
        } catch (DataAccessException e) {
            log.error("failed to delete exercise = {}", exercise, e);
            throw new ServiceException("Failed to delete exercise", e);
        }
    }

    @Override
    public Exercise getById(long id) {
        log.debug("Trying to get exercise with id={}", id);

        if (id == 0) {
            log.warn(MISSING_ID_ERROR_MESSAGE);
            throw new ServiceException(MISSING_ID_ERROR_MESSAGE);
        }
        Exercise exercise;
        try {
            exercise = exerciseRepository.findById(id)
                    .orElseThrow(() -> new NoSuchEntityException("Invalid exercise ID"));
        } catch (EmptyResultDataAccessException e) {
            log.warn("Not existing exercise with id={}", id);
            throw new NoSuchEntityException(NOT_EXIST_ENTITY);
        } catch (DataAccessException e) {
            log.error("Failed to retrieve exercise with id={}", id, e);
            throw new ServiceException("Failed to retrieve exercise with such id", e);
        }
        return exercise;
    }

    @Override
    public List<Exercise> findByKeyword(String keyword) {
        return exerciseRepository.findByKeyword(keyword);
    }
}
