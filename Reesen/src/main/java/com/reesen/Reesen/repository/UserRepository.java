package com.reesen.Reesen.repository;

import com.reesen.Reesen.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.Set;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    @Query("select COUNT(u) from User u where not u.role='ADMIN'")
    Integer getNumberOfUsers();

    @Query("select u.isBlocked from User u where u.id=:id")
    boolean getIsBlocked(Long id);

    @Transactional
    @Modifying
    @Query("update User u set u.password=:password where u.id=:id")
    void changePassword(String password, Long id);

    @Query("select u.password from User u where u.id=:id")
    String getUserPassword(Long id);
}
