package com.example.springsecuritydemo.service.impl;

import com.example.springsecuritydemo.exception.UserAlreadyExistException;
import com.example.springsecuritydemo.model.Client;
import com.example.springsecuritydemo.model.dto.UserDto;

public interface IUserService {



    Client addClientInfo(Client client, String currentPrincipalName);

    Client registerNewClient(UserDto userDto) throws UserAlreadyExistException;
}
