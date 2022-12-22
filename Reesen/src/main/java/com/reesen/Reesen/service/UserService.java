package com.reesen.Reesen.service;

import com.reesen.Reesen.model.User;
import com.reesen.Reesen.repository.UserRepository;
import com.reesen.Reesen.service.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserService implements IUserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User save(User user) {
        return this.userRepository.save(user);
    }

    @Override
    public User findOne(Long id) {
        return this.userRepository.findById(id).orElseGet(null);
    }

    @Override
    public Set<User> getUsers() {
        return new HashSet<>(this.userRepository.findAll());
    }


}
