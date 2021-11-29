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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@AllArgsConstructor
public class CoachServiceImpl implements ICoachService {

    private final CoachRepository coachRepository;


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





}
