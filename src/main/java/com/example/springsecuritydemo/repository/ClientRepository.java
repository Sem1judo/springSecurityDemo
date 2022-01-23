package com.example.springsecuritydemo.repository;

import com.example.springsecuritydemo.model.Client;
import com.example.springsecuritydemo.model.Status;
import com.example.springsecuritydemo.model.StatusCoach;
import com.example.springsecuritydemo.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;


public interface ClientRepository extends JpaRepository<Client, Long> {
    Optional<Client> findByEmail(String email);


    List<Client> findAllByStatusCoach(StatusCoach statusCoach);


}
