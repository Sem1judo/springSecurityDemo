package com.example.springsecuritydemo.service;

import com.example.springsecuritydemo.exception.UserAlreadyExistException;
import com.example.springsecuritydemo.model.Coach;
import com.example.springsecuritydemo.model.User;
import com.example.springsecuritydemo.model.dto.UserDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IUserService {


    <T extends User> User registerNewUser(UserDto userDto) throws UserAlreadyExistException;

    List<User> getListUser();

    void update(UserDto userDto);

    void deleteById(long id);

    void delete(User user);

    UserDto getByIdUserConvertedToUserDto(long id);

    User getUserByEmail(String email);

    void changeUserPassword(User user, String password);
}
