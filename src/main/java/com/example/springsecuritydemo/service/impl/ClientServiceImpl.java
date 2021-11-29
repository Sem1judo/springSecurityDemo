package com.example.springsecuritydemo.service.impl;

import com.example.springsecuritydemo.exception.NoSuchEntityException;
import com.example.springsecuritydemo.exception.ServiceException;
import com.example.springsecuritydemo.exception.UserAlreadyExistException;
import com.example.springsecuritydemo.model.Client;
import com.example.springsecuritydemo.model.dto.UserDto;
import com.example.springsecuritydemo.repository.ClientRepository;
import com.example.springsecuritydemo.service.IClientService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class ClientServiceImpl implements IClientService {

    private ClientRepository clientRepository;



    @Override
    public Client addClientInfo(Client client, String currentPrincipalName) {
        log.debug("Trying to add client: {}", client);

        Client currentClient;
        try {
            currentClient = clientRepository.findByEmail(currentPrincipalName)
                    .orElseThrow(() -> new NoSuchEntityException("Invalid client email"));
        } catch (EmptyResultDataAccessException e) {
            log.warn("Doesn't exist such client: {}", client);
            throw new NoSuchEntityException("Doesn't exist such client with email" + currentPrincipalName);
        } catch (DataAccessException e) {
            log.error("Failed to retrieve user: {}", client);
            throw new ServiceException("Failed to retrieve client: ", e);
        }
        try {
            currentClient.setWeight(client.getWeight());
            currentClient.setHeight(client.getHeight());
            clientRepository.save(currentClient);
        } catch (DataAccessException e) {
            log.error("Failed to add client: {}", client);
            throw new ServiceException("Problem with adding client");
        }
        return client;
    }



}
