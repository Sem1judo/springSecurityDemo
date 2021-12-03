package com.example.springsecuritydemo.service.impl;

import com.example.springsecuritydemo.exception.NoSuchEntityException;
import com.example.springsecuritydemo.exception.ServiceException;
import com.example.springsecuritydemo.exception.UserAlreadyExistException;
import com.example.springsecuritydemo.model.Client;
import com.example.springsecuritydemo.model.Coach;
import com.example.springsecuritydemo.model.dto.UserDto;
import com.example.springsecuritydemo.repository.ClientRepository;
import com.example.springsecuritydemo.service.IClientService;
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
public class ClientServiceImpl implements IClientService {

    private ClientRepository clientRepository;

    @Override
    public Page<Client> findPaginated(int pageNo, Integer pageSize, String sortField, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
                Sort.by(sortField).descending();

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        return clientRepository.findAll(pageable);

    }

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

    public Client getByIdClient(long id) {
        log.debug("Trying to get client with id={}", id);

        if (id == 0) {
            log.warn("Missing id client");
            throw new ServiceException("Missing id client");
        }
        Client client;
        try {
            client = clientRepository.findById(id)
                    .orElseThrow(() -> new NoSuchEntityException("Invalid client ID"));
        } catch (EmptyResultDataAccessException e) {
            log.warn("Not existing client with id={}", id);
            throw new NoSuchEntityException("Not existing client with id ="+id);
        } catch (DataAccessException e) {
            log.error("Failed to retrieve client with id={}", id, e);
            throw new ServiceException("Failed to retrieve client with id", e);
        }
        return client;
    }



}
