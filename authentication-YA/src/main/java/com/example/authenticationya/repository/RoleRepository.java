package com.example.authenticationya.repository;

import com.example.authenticationya.entity.Role;
import com.example.authenticationya.entity.enums.Erole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    @Query("select r from Role r where r.role = ?1")
    Optional<Role> findByRole(Erole role);
}
