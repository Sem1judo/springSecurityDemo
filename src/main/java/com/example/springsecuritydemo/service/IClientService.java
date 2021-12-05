package com.example.springsecuritydemo.service;

import com.example.springsecuritydemo.exception.UserAlreadyExistException;
import com.example.springsecuritydemo.model.Client;
import com.example.springsecuritydemo.model.User;
import com.example.springsecuritydemo.model.dto.UserDto;
import org.springframework.data.domain.Page;

public interface IClientService {



    Client addClientInfo(Client client, String currentPrincipalName);

}
