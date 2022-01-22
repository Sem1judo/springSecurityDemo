package com.example.springsecuritydemo.repository;

import com.example.springsecuritydemo.model.Exercise;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface ExerciseRepository extends PagingAndSortingRepository<Exercise, Long> {

    @Query(value = "select * from exercises e where " +
            "(lower(e.name) like lower(concat('%', :keyword,'%')))", nativeQuery = true)
    List<Exercise> findByKeyword(@Param("keyword") String keyword);

}
