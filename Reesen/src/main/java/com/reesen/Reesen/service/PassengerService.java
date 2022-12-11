package com.reesen.Reesen.service;

import com.reesen.Reesen.model.Passenger;
import com.reesen.Reesen.repository.PassengerRepository;
import com.reesen.Reesen.service.interfaces.IPassengerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public Passenger findOne(Long id) {
        return this.passengerRepository.findById(id).orElseGet(null);
    }
}
