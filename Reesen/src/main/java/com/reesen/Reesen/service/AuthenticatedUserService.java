package com.reesen.Reesen.service;

import com.reesen.Reesen.model.User;
import com.reesen.Reesen.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticatedUserService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public AuthenticatedUserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        Optional<User> user = this.userRepository.findByEmail(username);
        if(user.isEmpty()) throw new UsernameNotFoundException("User not found with email: " + username);

        return org.springframework.security.core.userdetails.User
                .withUsername(username)
                .password(user.get().getPassword())
                .roles(user.get().getRole().toString())
                .build();
    }
}
