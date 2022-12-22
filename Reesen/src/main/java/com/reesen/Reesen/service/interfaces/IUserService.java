package com.reesen.Reesen.service.interfaces;

import com.reesen.Reesen.model.User;

import java.util.Set;

public interface IUserService {
    User save(User user);
    User findOne(Long id);
    Set<User> getUsers();


}
