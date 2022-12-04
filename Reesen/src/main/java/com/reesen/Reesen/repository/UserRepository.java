package com.reesen.Reesen.repository;

import com.reesen.Reesen.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
