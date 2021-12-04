package com.example.springsecuritydemo.repository;

import com.example.springsecuritydemo.model.Coach;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface CoachRepository extends JpaRepository<Coach, Long> {
    Optional<Coach> findByEmail(String email);


    @Query(value = "select * from users u where u.first_name like %:keyword% or u.last_name like %:keyword%", nativeQuery = true)
    List<Coach> findByKeyword(@Param("keyword") String keyword);
}
