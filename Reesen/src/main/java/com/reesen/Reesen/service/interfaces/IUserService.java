package com.reesen.Reesen.service.interfaces;

import com.reesen.Reesen.model.ResetPasswordToken;
import com.reesen.Reesen.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    Page<User> findAll(Pageable page);

    Integer getTotalNumberOfUsers();

    boolean getIsUserBlocked(Long id);

    boolean changePassword(String old_password, String new_password, Long id);

    void resetPassword(String password, Long id);

    void saveResetPasswordToken(ResetPasswordToken resetPasswordToken);

    ResetPasswordToken findByUserIdAndCode(Long id, String code);
    
    int getTotalNumberOfRides(User user);
}
