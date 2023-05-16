package com.balukonis.app.universitymicroserviceUI.service;

import com.balukonis.app.universitymicroserviceUI.model.User;
import com.balukonis.app.universitymicroserviceUI.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class UserService implements IUserService{

    @Autowired
    private UserRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User registerNewUserAccount(User user) {
       User newUser = new User();
       newUser.setUsername(user.getUsername());
       newUser.setPassword(passwordEncoder.encode(user.getPassword()));
       newUser.setRole(user.getRole());

        return repository.save(newUser);
    }
}
