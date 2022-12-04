package com.reesen.Reesen.service.interfaces;

import com.reesen.Reesen.model.User;

public interface IUserService {
    User save(User user);
    User findOne(Long id);

}
