package com.reesen.Reesen.service;

import com.reesen.Reesen.Enums.Role;
import com.reesen.Reesen.dto.PassengerDTO;
import com.reesen.Reesen.exceptions.EmailNotConfirmedException;
import com.reesen.Reesen.model.Passenger;
import com.reesen.Reesen.repository.PassengerRepository;
import com.reesen.Reesen.service.interfaces.IPassengerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PassengerService implements IPassengerService {
    private final PassengerRepository passengerRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public PassengerService(PassengerRepository passengerRepository, PasswordEncoder passwordEncoder){
        this.passengerRepository = passengerRepository;
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
        passenger.setId(passengerDTO.getId());
        passenger.setName(passengerDTO.getName());
        passenger.setSurname(passengerDTO.getSurname());
        passenger.setProfilePicture(passengerDTO.getProfilePicture());
        passenger.setTelephoneNumber(passengerDTO.getTelephoneNumber());
        passenger.setEmail(passengerDTO.getEmail());
        passenger.setAddress(passengerDTO.getAddress());
        if(passengerDTO.getPassword() != null)
            passenger.setPassword(passwordEncoder.encode(passengerDTO.getPassword()));
        return passenger;
    }
    @Override
    public boolean getIsEmailConfirmed(String username){
        return this.passengerRepository.getEmailConfirmation(username);
    }
}
