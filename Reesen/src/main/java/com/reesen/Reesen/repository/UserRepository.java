package com.reesen.Reesen.repository;

import com.reesen.Reesen.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

public interface UserRepository extends JpaRepository<User, Long> {

        public Optional<User> findByEmail(String email);
}
