package com.springsecurity.jwt.services;

import com.springsecurity.jwt.repository.SpringSecurityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class MyUserDetailService implements UserDetailsService {

    @Autowired
    SpringSecurityRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<com.springsecurity.jwt.models.User> optionalUser = repository.findUserByUsername(username);
        com.springsecurity.jwt.models.User user = new com.springsecurity.jwt.models.User();
        if (optionalUser.isPresent()) {
            user = optionalUser.get();
        }
        return new User(user.getUsername(), user.getPassword(), new ArrayList<>());
    }

    public com.springsecurity.jwt.models.User createUser(com.springsecurity.jwt.models.User user) {
        return repository.save(user);
    }
}
