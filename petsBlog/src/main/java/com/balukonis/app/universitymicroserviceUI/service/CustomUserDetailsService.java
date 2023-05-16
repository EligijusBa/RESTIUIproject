package com.balukonis.app.universitymicroserviceUI.service;

import com.balukonis.app.universitymicroserviceUI.Mapper.UserDetailsMapper;
import com.balukonis.app.universitymicroserviceUI.model.User;
import com.balukonis.app.universitymicroserviceUI.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository
                .findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("No user was found by given username" + username));
        return new UserDetailsMapper(user);
    }
}
