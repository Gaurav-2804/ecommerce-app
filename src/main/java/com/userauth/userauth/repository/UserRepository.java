package com.userauth.userauth.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.userauth.userauth.entity.User;

public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
