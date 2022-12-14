package com.reesen.Reesen.service;

import com.reesen.Reesen.dto.PassengerDTO;
import com.reesen.Reesen.model.Passenger;
import com.reesen.Reesen.model.Ride;
import com.reesen.Reesen.repository.PassengerRepository;
import com.reesen.Reesen.service.interfaces.IPassengerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PassengerService implements IPassengerService {
    private final PassengerRepository passengerRepository;

    @Autowired
    public PassengerService(PassengerRepository passengerRepository){
        this.passengerRepository = passengerRepository;
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
        passenger.setPassword(passengerDTO.getPassword());
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
        passenger.setPassword(passengerDTO.getPassword());
        return passenger;
    }

}
