package com.reesen.Reesen.service.interfaces;

import com.reesen.Reesen.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;
import java.util.Set;

public interface IUserService {
    Optional<User> findByEmail(String email);
    UserDetails findByUsername(String username) throws UsernameNotFoundException;
    User save(User user);
    User findOne(Long id);
    Set<User> getUsers();


}
