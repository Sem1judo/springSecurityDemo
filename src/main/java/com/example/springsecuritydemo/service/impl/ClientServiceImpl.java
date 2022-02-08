package com.example.springsecuritydemo.service.impl;

import com.example.springsecuritydemo.exception.NoSuchEntityException;
import com.example.springsecuritydemo.exception.ServiceException;
import com.example.springsecuritydemo.model.Client;
import com.example.springsecuritydemo.model.Coach;
import com.example.springsecuritydemo.model.StatusCoach;
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
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class ClientServiceImpl implements IClientService {


    private static final String MISSING_EMAIL_ERROR_MESSAGE = "Missing email client";
    private static final String NOT_EXIST_ENTITY = "Doesn't exist such client";

    private final ClientRepository clientRepository;
    private final CoachServiceImpl coachService;


    @Override
    public List<Client> findByStatusCoach(StatusCoach statusCoach) {
        return clientRepository.findAllByStatusCoach(statusCoach);
    }

    @Override
    public List<Client> getListClient() {
        log.debug("Trying to get list of Clients");
        try {
            return clientRepository.findAll();
        } catch (EmptyResultDataAccessException e) {
            log.warn("Clients is not exist");
            throw new NoSuchEntityException("Doesn't exist such Clients");
        } catch (DataAccessException e) {
            log.error("Failed to get list of Clients", e);
            throw new ServiceException("Failed to get list of Clients", e);
        }
    }

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

    @Override
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
            throw new NoSuchEntityException("Not existing client with id =" + id);
        } catch (DataAccessException e) {
            log.error("Failed to retrieve client with id={}", id, e);
            throw new ServiceException("Failed to retrieve client with id", e);
        }
        return client;
    }

    @Override
    public Client getClientByEmail(String email) {
        log.debug("Trying to get client with email={}", email);

        if (email.isEmpty()) {
            log.warn(MISSING_EMAIL_ERROR_MESSAGE);
            throw new ServiceException(MISSING_EMAIL_ERROR_MESSAGE);
        }
        Client client;
        try {
            client = clientRepository.findByEmail(email)
                    .orElseThrow(() -> new NoSuchEntityException("Invalid client email"));
        } catch (EmptyResultDataAccessException e) {
            log.warn("Not existing client with id={}", email);
            throw new NoSuchEntityException(NOT_EXIST_ENTITY);
        } catch (DataAccessException e) {
            log.error("Failed to retrieve client with email={}", email, e);
            throw new ServiceException("Failed to retrieve client with such email", e);
        }
        return client;
    }

    @Override
    public void addCoachForUser(Long coachId, String authCurrentEmail) {
        log.debug("Trying to add Coach For User with coach.id={}", coachId);

        if (coachId == 0) {
            log.warn("Missing id coach");
            throw new ServiceException("Missing id coach");
        }
        Client client = getClientByEmail(authCurrentEmail);
        Coach coach = coachService.getByIdCoach(coachId);
        client.setCoach(coach);
        client.setStatusCoach(StatusCoach.WAITING);
        try {
            clientRepository.save(client);
        } catch (DataAccessException e) {
            log.error("Failed to add coach to client: {}", client);
            throw new ServiceException("Problem with adding coach to client");
        }


    }

    @Override
    public void attachCoachForUser(Long clientId) {
        log.debug("Trying to confirm Coach For User with coach.id={}", clientId);

        if (clientId == 0) {
            log.warn("Missing clientId");
            throw new ServiceException("Missing clientId");
        }
        Client client = getByIdClient(clientId);
        client.setStatusCoach(StatusCoach.ATTACHED);
        try {

            clientRepository.save(client);
        } catch (DataAccessException e) {
            log.error("Failed to attach coach to client: {}", client);
            throw new ServiceException("Problem with attach coach to client");
        }
    }

    @Override
    public void declineCoachForUser(Long clientId) {
        log.debug("Trying to decline Coach For User with clientId={}", clientId);

        if (clientId == 0) {
            log.warn("Missing clientId");
            throw new ServiceException("Missing clientId");
        }
        Client client = getByIdClient(clientId);
        client.setStatusCoach(StatusCoach.DECLINED);
        try {
            clientRepository.save(client);
        } catch (DataAccessException e) {
            log.error("Failed to decline coach from client: {}", client);
            throw new ServiceException("Problem with declining coach from client");
        }

    }

    @Override
    public void deleteCoachForUser(String authCurrentEmail, Long coachId) {
        log.debug("Trying to delete Coach For User with coach.id={}", coachId);

        if (coachId == 0) {
            log.warn("Missing id coach");
            throw new ServiceException("Missing id coach");
        }
        Client client = getClientByEmail(authCurrentEmail);
        client.setCoach(null);
        client.setStatusCoach(StatusCoach.EMPTY);
        try {
            clientRepository.save(client);
        } catch (DataAccessException e) {
            log.error("Failed to deleting coach for client: {}", client);
            throw new ServiceException("Problem with deleting coach for client");
        }
    }

    public void deleteCoachForClientByAdmin(Long clientId) {
        log.debug("Trying to delete Coach For User with client.id={}", clientId);

        if (clientId == 0) {
            log.warn("Missing id client");
            throw new ServiceException("Missing id client");
        }
        Client client = getByIdClient(clientId);
        client.setCoach(null);
        client.setStatusCoach(StatusCoach.DECLINED);
        try {
            clientRepository.save(client);
        } catch (DataAccessException e) {
            log.error("Failed to deleting coach for client: {}", client);
            throw new ServiceException("Problem with deleting coach for client");
        }
    }

}
