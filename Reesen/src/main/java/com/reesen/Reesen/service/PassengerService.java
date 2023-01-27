package com.reesen.Reesen.service;

import com.reesen.Reesen.Enums.Role;
import com.reesen.Reesen.dto.PassengerDTO;
import com.reesen.Reesen.model.FavoriteRide;
import com.reesen.Reesen.model.Passenger;
import com.reesen.Reesen.model.Ride;
import com.reesen.Reesen.model.VerificationToken;
import com.reesen.Reesen.repository.PassengerRepository;
import com.reesen.Reesen.repository.VerificationTokenRepository;
import com.reesen.Reesen.service.interfaces.IPassengerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class PassengerService implements IPassengerService {
    private final PassengerRepository passengerRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public PassengerService(PassengerRepository passengerRepository, VerificationTokenRepository verificationTokenRepository, PasswordEncoder passwordEncoder){
        this.passengerRepository = passengerRepository;
        this.verificationTokenRepository = verificationTokenRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Passenger save(Passenger passenger) {
        return this.passengerRepository.save(passenger);
    }

    @Override
    public Optional<Passenger> findOne(Long id) {
        return this.passengerRepository.findById(id);
    }

    @Override
    public Passenger findByEmail(String email) {
        return this.passengerRepository.findByEmail(email);
    }

    @Override
    public PassengerDTO createPassengerDTO(PassengerDTO passengerDTO) {
        Passenger passenger = new Passenger();
        passenger.setId(Long.parseLong("546"));
        passenger.setName(passengerDTO.getName());
        passenger.setSurname(passengerDTO.getSurname());
        passenger.setProfilePicture(passengerDTO.getProfilePicture());
        passenger.setTelephoneNumber(passengerDTO.getTelephoneNumber());
        passenger.setEmail(passengerDTO.getEmail());
        passenger.setAddress(passengerDTO.getAddress());
        passenger.setPassword(passwordEncoder.encode(passengerDTO.getPassword()));
        passenger.setRole(Role.PASSENGER);
        passenger.setConfirmedMail(false);
        return new PassengerDTO(this.passengerRepository.save(passenger));
    }

    @Override
    public Page<Passenger> findAll(Pageable page) {
        return this.passengerRepository.findAll(page);
    }

    @Override
    public Passenger getPassengerFromPassengerDTO(Long id, PassengerDTO passengerDTO) {
        Passenger passenger = new Passenger();
        passenger.setId(id);
        passenger.setName(passengerDTO.getName());
        passenger.setSurname(passengerDTO.getSurname());
        passenger.setProfilePicture(passengerDTO.getProfilePicture());
        passenger.setTelephoneNumber(passengerDTO.getTelephoneNumber());
        passenger.setEmail(passengerDTO.getEmail());
        passenger.setAddress(passengerDTO.getAddress());
        passenger.setConfirmedMail(true);
        passenger.setPassword(this.passengerRepository.getPasswordWithId(id));
        passenger.setRole(Role.PASSENGER);
        Set<Ride> rides = this.passengerRepository.getPassengerRides(passenger.getId());
        passenger.setRides(rides);
        return passenger;
    }
    @Override
    public boolean getIsEmailConfirmed(String username){
        Boolean exists = this.passengerRepository.getEmailConfirmation(username);
        if(exists == null) return true;
        return exists;
        }

    @Override
    public Set<Passenger> findPassengersByRidesContaining(Ride ride) {
        return this.passengerRepository.findPassengersByRidesContaining(ride);

    }

    @Override
    public void saveVerificationToken(VerificationToken verificationToken) {
        this.verificationTokenRepository.save(verificationToken);
    }

    @Override
    public VerificationToken findByUrl(String url) {
        return this.verificationTokenRepository.findByUrl(url);
    }

    @Override
    public Set<FavoriteRide> getFavoriteRides(Long id) {
        return this.passengerRepository.getPassengerFavoriteRides(id);
    }

    @Override
    public void activateAccount(Long passengerId) {
        this.passengerRepository.activateAccount(passengerId);
    }
}
