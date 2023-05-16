package com.balukonis.app.universitymicroserviceUI.repository;

import com.balukonis.app.universitymicroserviceUI.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
    Optional<User> findByUsername(String username);
}
