package com.reesen.Reesen.service;

import com.reesen.Reesen.Enums.Role;
import com.reesen.Reesen.model.User;
import com.reesen.Reesen.repository.DriverRepository;
import com.reesen.Reesen.repository.PassengerRepository;
import com.reesen.Reesen.repository.UserRepository;
import com.reesen.Reesen.security.SecurityUser;
import com.reesen.Reesen.security.UserFactory;
import com.reesen.Reesen.service.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService implements IUserService {


    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final DriverRepository driverRepository;
    private final PassengerRepository passengerRepository;


    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, DriverRepository driverRepository, PassengerRepository passengerRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.driverRepository = driverRepository;
        this.passengerRepository = passengerRepository;
    }

    @Override
    public SecurityUser findByUsername(String username) throws UsernameNotFoundException {
        User user = this.userRepository.findByEmail(username).orElseThrow(
                () -> new UsernameNotFoundException(String.format("User with username '%s' is not found!", username)));

        return UserFactory.create(user);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return this.userRepository.findByEmail(email);
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

    @Override
    public Page<User> findAll(Pageable page) {
        return this.userRepository.findAll(page);
    }

    @Override
    public Integer getTotalNumberOfUsers() {
        return this.userRepository.getNumberOfUsers();
    }

    @Override
    public boolean getIsUserBlocked(Long id) {
        return this.userRepository.getIsBlocked(id);
    }

    @Override
    public boolean changePassword(String old_password, String new_password, Long id) {

        String old = this.userRepository.getUserPassword(id);
        System.out.println(old_password);
        System.out.println(passwordEncoder.encode(old_password));
        System.out.println(old);
        if(!passwordEncoder.matches(old_password, old)) return false;

        this.userRepository.changePassword(passwordEncoder.encode(new_password), id);
        return true;
    }

    @Override
    public void resetPassword(String password, Long id) {
        this.userRepository.changePassword(passwordEncoder.encode(password), id);
    }

    @Override
    public int getTotalNumberOfRides(User user) {
        if(user.getRole() == Role.DRIVER)
            return this.driverRepository.countTotalNumberOfRides(user.getId());

        return this.passengerRepository.countTotalNumberOfRides(user.getId());

    }

}
