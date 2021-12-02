package com.example.springsecuritydemo.service;

import com.example.springsecuritydemo.exception.UserAlreadyExistException;
import com.example.springsecuritydemo.model.Client;
import com.example.springsecuritydemo.model.Coach;
import org.springframework.data.domain.Page;

public interface ICoachService {
    Page<Coach> findPaginated(int pageNo, Integer pageSize, String sortField, String sortDirection);

    Coach addCoachInfo(Coach coach, String currentPrincipalName);

}
