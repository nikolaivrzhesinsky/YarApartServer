package com.example.authenticationya.repository;

import com.example.authenticationya.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {


    @Query("select u from User u where u.userName = ?1")
    Optional<User> findByUserName(String userName);

    @Query("select (count(u) > 0) from User u where u.userName = ?1")
    boolean existsByUserName(String userName);

    @Query("select (count(u) > 0) from User u where u.email = ?1")
    boolean existsByEmail(String email);

    @Query("select u from User u where u.activationCode = ?1")
    Optional<User> findByActivationCode(String code);
}
