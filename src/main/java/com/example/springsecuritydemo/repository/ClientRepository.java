package com.example.springsecuritydemo.repository;

import com.example.springsecuritydemo.model.Client;
import com.example.springsecuritydemo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;


public interface ClientRepository extends JpaRepository<Client, Long> {
    Optional<Client> findByEmail(String email);
}
