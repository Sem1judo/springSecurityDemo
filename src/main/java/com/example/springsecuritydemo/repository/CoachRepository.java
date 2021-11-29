package com.example.springsecuritydemo.repository;

import com.example.springsecuritydemo.model.Coach;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;


public interface CoachRepository extends CrudRepository<Coach, Long> {
    Optional<Coach> findByEmail(String email);
}
