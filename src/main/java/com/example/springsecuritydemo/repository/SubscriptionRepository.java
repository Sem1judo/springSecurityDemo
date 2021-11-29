package com.example.springsecuritydemo.repository;


import com.example.springsecuritydemo.model.Subscription;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Semkov.S
 */
@Repository
public interface SubscriptionRepository extends CrudRepository<Subscription, Long> {


}
