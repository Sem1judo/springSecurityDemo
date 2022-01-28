package com.example.springsecuritydemo.service.impl;

import com.example.springsecuritydemo.exception.InvalidOldPasswordException;
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
import java.util.Optional;

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

    public void update(UserDto userDto) {
        log.debug("Trying to update userDto: {}", userDto);

        if (userDto.getId() == 0) {
            log.warn(MISSING_ID_ERROR_MESSAGE);
            throw new ServiceException(MISSING_ID_ERROR_MESSAGE);
        }

        try {
            userRepository.findById(userDto.getId());
        } catch (EmptyResultDataAccessException e) {
            log.warn("Not existing userDto: {}", userDto);
            throw new NoSuchEntityException(NOT_EXIST_ENTITY);
        } catch (DataAccessException e) {
            log.error("Failed to retrieve userDto: {}", userDto);
            throw new ServiceException("Failed to retrieve userDto: ", e);
        }
        try {
            if (userDto.getTypeUser().equals(TypeUser.CLIENT)) {
                clientRepository.save((Client) userDtoConvertToEntity(userDto));
            }
            if (userDto.getTypeUser().equals(TypeUser.COACH)) {
                coachRepository.save((Coach) userDtoConvertToEntity(userDto));
            }
        } catch (DataAccessException e) {
            log.error("Failed to update userDto: {}", userDto);
            throw new ServiceException("Problem with updating userDto");
        }
    }


    public void deleteById(long id) {
        log.debug("Trying to delete user with id={}", id);
        if (id == 0) {
            log.warn(MISSING_ID_ERROR_MESSAGE);
            throw new ServiceException(MISSING_ID_ERROR_MESSAGE);
        }
        try {
            userRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            log.warn("Not existing user with id={}", id);
            throw new NoSuchEntityException(NOT_EXIST_ENTITY);
        } catch (DataAccessException e) {
            log.error("failed to delete user with id={}", id, e);
            throw new ServiceException("Failed to delete user by id", e);
        }
    }

    public void delete(User user) {
        log.debug("Trying to delete user = {}", user);

        if (user == null) {
            log.warn(MISSING_ID_ERROR_MESSAGE);
            throw new ServiceException(MISSING_ID_ERROR_MESSAGE);
        }
        try {
            userRepository.delete(user);
        } catch (EmptyResultDataAccessException e) {
            log.warn("Not existing user = {}", user);
            throw new NoSuchEntityException(NOT_EXIST_ENTITY);
        } catch (DataAccessException e) {
            log.error("failed to delete user = {}", user, e);
            throw new ServiceException("Failed to delete user", e);
        }
    }


    public UserDto getByIdUserConvertedToUserDto(long id) {
        log.debug("Trying to get user with id={}", id);

        if (id == 0) {
            log.warn(MISSING_ID_ERROR_MESSAGE);
            throw new ServiceException(MISSING_ID_ERROR_MESSAGE);
        }
        UserDto userDto;
        try {
            userDto = entityConvertToUserDto(userRepository.findById(id)
                    .orElseThrow(() -> new NoSuchEntityException("Invalid user ID")));
        } catch (EmptyResultDataAccessException e) {
            log.warn("Not existing user with id={}", id);
            throw new NoSuchEntityException(NOT_EXIST_ENTITY);
        } catch (DataAccessException e) {
            log.error("Failed to retrieve user with id={}", id, e);
            throw new ServiceException("Failed to retrieve user with such id", e);
        }
        return userDto;
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
            log.warn("Not existing user with id={}", email);
            throw new NoSuchEntityException(NOT_EXIST_ENTITY);
        } catch (DataAccessException e) {
            log.error("Failed to retrieve user with email={}", email, e);
            throw new ServiceException("Failed to retrieve user with such email", e);
        }
        return user;
    }


    public void changeUserPassword(final User user, final String password) {
        user.setPassword(bcryptEncoder.encode(password));
        userRepository.save(user);
    }


    private <T extends User> UserDto entityConvertToUserDto(T t) {
        UserDto userDto = new UserDto();

        userDto.setId(t.getId());
        userDto.setEmail(t.getEmail());
        userDto.setPassword(t.getPassword());
        userDto.setFirstName(t.getFirstName());
        userDto.setLastName(t.getLastName());
        userDto.setPhone(t.getPhone());
        userDto.setBirthDate(t.getBirthDate());
        userDto.setGender(t.getGender());

        if (t instanceof Client) {
            userDto.setTypeUser(TypeUser.CLIENT);
            userDto.setHeight(((Client) t).getHeight());
            userDto.setWeight(((Client) t).getWeight());
            userDto.setStatusCoach(((Client) t).getStatusCoach());
            userDto.setCoach(((Client) t).getCoach());
            userDto.setExercises(((Client) t).getExercises());
        }
        if (t instanceof Coach) {
            userDto.setTypeUser(TypeUser.COACH);
            userDto.setEducation(((Coach) t).getEducation());
            userDto.setAchievement(((Coach) t).getAchievement());
            userDto.setAdditionalInfo(((Coach) t).getAdditionalInfo());
            userDto.setSpecialization(((Coach) t).getSpecialization());
        }

        return userDto;
    }


    private <T> User userDtoConvertToEntity(UserDto userDto) {
        if (userDto != null) {
            if (userDto.getTypeUser().equals(TypeUser.CLIENT)) {
                Client client = new Client();
                setUserValues(userDto, client);
                client.setHeight(userDto.getHeight());
                client.setWeight(userDto.getWeight());
                client.setStatusCoach(userDto.getStatusCoach());
                client.setCoach(userDto.getCoach());
                client.setExercises(userDto.getExercises());
                return client;
            }
            if (userDto.getTypeUser().equals(TypeUser.COACH)) {
                Coach coach = new Coach();
                setUserValues(userDto, coach);
                coach.setEducation(userDto.getEducation());
                coach.setAchievement(userDto.getAchievement());
                coach.setAdditionalInfo(userDto.getAdditionalInfo());
                coach.setSpecialization(userDto.getSpecialization());
                return coach;
            }
        }
        return null;
    }

    private <T extends User> User setUserValues(UserDto userDto, T t) {

        if (userDto.getId() != null) {
            t.setId(userDto.getId());
        }
        if (!userDto.getPassword().startsWith("$2a$12")) {
            t.setPassword(bcryptEncoder.encode(userDto.getPassword()));
        } else {
            t.setPassword(userDto.getPassword());
        }
        t.setEmail(userDto.getEmail());
        t.setFirstName(userDto.getFirstName());
        t.setLastName(userDto.getLastName());
        t.setPhone(userDto.getPhone());
        t.setBirthDate(userDto.getBirthDate());
        t.setGender(userDto.getGender());
        return t;
    }


    public boolean checkIfValidOldPassword(final User user, final String oldPassword) throws InvalidOldPasswordException {
        return bcryptEncoder.matches(oldPassword, user.getPassword());
    }


}
