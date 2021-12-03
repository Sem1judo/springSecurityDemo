package com.example.springsecuritydemo.service.impl;

import com.example.springsecuritydemo.exception.NoSuchEntityException;
import com.example.springsecuritydemo.exception.ServiceException;
import com.example.springsecuritydemo.exception.UserAlreadyExistException;
import com.example.springsecuritydemo.model.Client;
import com.example.springsecuritydemo.model.Coach;
import com.example.springsecuritydemo.model.User;
import com.example.springsecuritydemo.model.dto.UserDto;
import com.example.springsecuritydemo.repository.CoachRepository;
import com.example.springsecuritydemo.service.ICoachService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@AllArgsConstructor
public class CoachServiceImpl implements ICoachService {

    private final CoachRepository coachRepository;


    @Override
    public Page<Coach> findPaginated(int pageNo, Integer pageSize, String sortField, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
                Sort.by(sortField).descending();

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        return this.coachRepository.findAll(pageable);

    }


    @Override
    public Coach addCoachInfo(Coach coach, String currentPrincipalName) {
        log.debug("Trying to add coach: {}", coach);

        Coach currCoach;
        try {
            currCoach = coachRepository.findByEmail(currentPrincipalName)
                    .orElseThrow(() -> new NoSuchEntityException("Invalid coach email"));
        } catch (EmptyResultDataAccessException e) {
            log.warn("Doesn't exist such coach: {}", coach);
            throw new NoSuchEntityException("Doesn't exist such coach with email" + currentPrincipalName);
        } catch (DataAccessException e) {
            log.error("Failed to retrieve user: {}", coach);
            throw new ServiceException("Failed to retrieve coach: ", e);
        }
        try {
            coachRepository.save(currCoach);
        } catch (DataAccessException e) {
            log.error("Failed to add coach: {}", coach);
            throw new ServiceException("Problem with adding coach");
        }
        return coach;
    }


    public Coach getByIdCoach(long id) {
        log.debug("Trying to get user with id={}", id);

        if (id == 0) {
            log.warn("Missing id coach");
            throw new ServiceException("Missing id coach");
        }
        Coach coach;
        try {
            coach = coachRepository.findById(id)
                    .orElseThrow(() -> new NoSuchEntityException("Invalid coach ID"));
        } catch (EmptyResultDataAccessException e) {
            log.warn("Not existing coach with id={}", id);
            throw new NoSuchEntityException("Not existing coach with id ="+id);
        } catch (DataAccessException e) {
            log.error("Failed to retrieve coach with id={}", id, e);
            throw new ServiceException("Failed to retrieve user with coach id", e);
        }
        return coach;
    }


}
