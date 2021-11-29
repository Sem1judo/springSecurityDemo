package com.example.springsecuritydemo.service.impl;

import com.example.springsecuritydemo.exception.NoSuchEntityException;
import com.example.springsecuritydemo.exception.ServiceException;
import com.example.springsecuritydemo.exception.UserAlreadyExistException;
import com.example.springsecuritydemo.model.Client;
import com.example.springsecuritydemo.model.Coach;
import com.example.springsecuritydemo.model.User;
import com.example.springsecuritydemo.model.dto.TypeUser;
import com.example.springsecuritydemo.model.dto.UserDto;
import com.example.springsecuritydemo.repository.ClientRepository;
import com.example.springsecuritydemo.repository.CoachRepository;
import com.example.springsecuritydemo.repository.UserRepository;
import com.example.springsecuritydemo.service.IUserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class UserServiceImpl implements IUserService {

    private UserRepository userRepository;
    private CoachRepository coachRepository;
    private ClientRepository clientRepository;

    private PasswordEncoder bcryptEncoder;


    private static final String MISSING_ID_ERROR_MESSAGE = "Missing id user";
    private static final String MISSING_EMAIL_ERROR_MESSAGE = "Missing email user";
    private static final String NOT_EXIST_ENTITY = "Doesn't exist such user";


    public <T extends User> User registerNewUser(UserDto userDto) throws UserAlreadyExistException {
        log.debug("Trying to register user: {}", userDto);

        if (userRepository.findByEmail(userDto.getEmail()).isPresent()) {
            throw new UserAlreadyExistException("User already exists with this email = \"" + userDto.getEmail() + "\"");
        }
        try {
            if (userDto.getTypeUser().equals(TypeUser.CLIENT)) {
                return clientRepository.save((Client) userDtoConvertToEntity(userDto));
            }
            if (userDto.getTypeUser().equals(TypeUser.COACH)) {
                return coachRepository.save((Coach) userDtoConvertToEntity(userDto));
            }
        } catch (
                DataAccessException e) {
            log.error("Failed to create user: {}", userDto, e);
            throw new ServiceException("Failed to create user", e);
        }
        return null;
    }


    public List<User> getListUser() {
        log.debug("Trying to get list of users");
        try {
            return (List<User>) userRepository.findAll();
        } catch (EmptyResultDataAccessException e) {
            log.warn("Users is not exist");
            throw new NoSuchEntityException("Doesn't exist such users");
        } catch (DataAccessException e) {
            log.error("Failed to get list of users", e);
            throw new ServiceException("Failed to get list of users", e);
        }
    }

    public List<User> getListUserPageable(Integer pageNo, Integer pageSize, String sortBy) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));

        Page<User> pagedResult = userRepository.findAll(paging);

        if (pagedResult.hasContent()) {
            return pagedResult.getContent();
        } else {
            return new ArrayList<>();
        }
    }

    public Page<User> findPaginated(Pageable pageable, List<User> users) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<User> listUser;

        if (users.size() < startItem) {
            listUser = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, users.size());
            listUser = users.subList(startItem, toIndex);
        }

        return new PageImpl<>(listUser, PageRequest.of(currentPage, pageSize), users.size());
    }


    public User findByEmail(String email) {
        return userRepository.findByEmail(email).get();
    }

    public User getUserByEmail(String email) {
        log.debug("Trying to get user with email={}", email);

        if (email.isEmpty()) {
            log.warn(MISSING_EMAIL_ERROR_MESSAGE);
            throw new ServiceException(MISSING_EMAIL_ERROR_MESSAGE);
        }
        User user;
        try {
            user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new NoSuchEntityException("Invalid user email"));
        } catch (EmptyResultDataAccessException e) {
            log.warn("Not existing event with id={}", email);
            throw new NoSuchEntityException(NOT_EXIST_ENTITY);
        } catch (DataAccessException e) {
            log.error("Failed to retrieve user with email={}", email, e);
            throw new ServiceException("Failed to retrieve user with such email", e);
        }
        return user;
    }


    private <T> User userDtoConvertToEntity(UserDto userDto) {
        if (userDto != null) {
            if (userDto.getTypeUser().equals(TypeUser.CLIENT)) {
                Client client = new Client();
                setUserValues(userDto, client);
                client.setHeight(userDto.getHeight());
                client.setWeight(userDto.getWeight());
                return client;
            }
            if (userDto.getTypeUser().equals(TypeUser.COACH)) {
                Coach coach = new Coach();
                setUserValues(userDto, coach);
                coach.setEducation(userDto.getEducation());
                coach.setAchievement(userDto.getAchievement());
                coach.setAdditionalInfo(userDto.getAdditionalInfo());
                return coach;
            }
        }
        return null;
    }

    private <T extends User> User setUserValues(UserDto userDto, T t) {
        t.setEmail(userDto.getEmail());
        t.setPassword(bcryptEncoder.encode(userDto.getPassword()));
        t.setFirstName(userDto.getFirstName());
        t.setLastName(userDto.getLastName());
        t.setPhone(userDto.getPhone());
        t.setBirthDate(userDto.getBirthDate());
        t.setGender(userDto.getGender());
        return t;
    }

//
//    public User findByLoginAndPassword(String login, String password) {
//        User userEntity = findByEmail(login);
//        if (userEntity != null) {
//            if (bcryptEncoder.matches(password, userEntity.getPassword())) {
//                return userEntity;
//            }
//        }
//        return null;
//    }


}
