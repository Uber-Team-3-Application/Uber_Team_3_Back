package com.reesen.Reesen.service;
import com.reesen.Reesen.Enums.VehicleName;
import com.reesen.Reesen.dto.DriveAssessmentDTO;
import com.reesen.Reesen.dto.EstimatedTimeDTO;
import com.reesen.Reesen.dto.LocationDTO;
import com.reesen.Reesen.dto.RouteDTO;
import com.reesen.Reesen.model.ResetPasswordToken;
import com.reesen.Reesen.model.User;
import com.reesen.Reesen.model.VehicleType;
import com.reesen.Reesen.repository.*;
import com.reesen.Reesen.Enums.Role;
import com.reesen.Reesen.model.User;
import com.reesen.Reesen.security.SecurityUser;
import com.reesen.Reesen.security.UserFactory;
import com.reesen.Reesen.service.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService implements IUserService {


    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ResetPasswordTokenRepository resetPasswordTokenRepository;
    private final DriverRepository driverRepository;
    private final PassengerRepository passengerRepository;
    private final VehicleRepository vehicleRepository;


    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, DriverRepository driverRepository,
                       PassengerRepository passengerRepository,
                       ResetPasswordTokenRepository resetPasswordTokenRepository,
                       VehicleRepository vehicleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.driverRepository = driverRepository;
        this.passengerRepository = passengerRepository;
        this.resetPasswordTokenRepository = resetPasswordTokenRepository;
        this.vehicleRepository = vehicleRepository;
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
        return this.userRepository.findById(id).orElse(null);
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
    public void saveResetPasswordToken(ResetPasswordToken resetPasswordToken) {
        this.resetPasswordTokenRepository.save(resetPasswordToken);
    }

    @Override
    public ResetPasswordToken findByUserIdAndCode(Long userId, String code) {
        return this.resetPasswordTokenRepository.findByUserIdAndCode(userId, code);
    }
    
    @Override
    public int getTotalNumberOfRides(User user) {
        if(user.getRole() == Role.DRIVER)
            return this.driverRepository.countTotalNumberOfRides(user.getId());

        return this.passengerRepository.countTotalNumberOfRides(user.getId());
    }

    @Override
    public Long getAdminId() {
        return this.userRepository.findAdmin(Role.ADMIN);
    }

    public static EstimatedTimeDTO getEstimatedTime(DriveAssessmentDTO driveAssessment, VehicleType vehicleType) {
        double amountDistance = 0;
        double estimatedCost = 140; // start
        ArrayList<RouteDTO> routes = new ArrayList<>(driveAssessment.getLocations());
        for (int i = 0; i < routes.size(); i++) {
            RouteDTO route = routes.get(i);
            LocationDTO location1 = route.getDeparture();
            LocationDTO location2 = route.getDestination();
            double theta = location1.getLongitude() - location2.getLongitude();
            double dist = Math.sin(Math.toRadians(location1.getLatitude())) * Math.sin(Math.toRadians(location2.getLatitude()))
                    + Math.cos(Math.toRadians(location1.getLatitude())) * Math.cos(Math.toRadians(location2.getLatitude())) * Math.cos(Math.toRadians(theta));
            dist = Math.acos(dist);
            dist = Math.toDegrees(dist);
            dist = dist * 60 * 1.1515;
            dist = dist * 1.609344;
            amountDistance += dist;
            estimatedCost += dist * vehicleType.getPricePerKm();
        }
        double estimatedTimeInMinutes = (amountDistance / 80) * 60 * 2;
        return new EstimatedTimeDTO((int) estimatedTimeInMinutes, (int)estimatedCost);
    }

}
