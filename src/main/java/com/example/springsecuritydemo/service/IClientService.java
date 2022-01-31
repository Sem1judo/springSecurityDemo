package com.example.springsecuritydemo.service;

import com.example.springsecuritydemo.exception.UserAlreadyExistException;
import com.example.springsecuritydemo.model.Client;
import com.example.springsecuritydemo.model.StatusCoach;
import com.example.springsecuritydemo.model.User;
import com.example.springsecuritydemo.model.dto.UserDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IClientService {


    List<Client> findByStatusCoach(StatusCoach statusCoach);

    List<Client> getListClient();

    Page<Client> findPaginated(int pageNo, Integer pageSize, String sortField, String sortDirection);

    Client addClientInfo(Client client, String currentPrincipalName);

    Client getByIdClient(long id);

    Client getClientByEmail(String email);

    void addCoachForUser(Long coachId, String authCurrentEmail);

    void attachCoachForUser(Long clientId);

    void declineCoachForUser(Long clientId);

    void deleteCoachForUser(String authCurrentEmail, Long coachId);
}
