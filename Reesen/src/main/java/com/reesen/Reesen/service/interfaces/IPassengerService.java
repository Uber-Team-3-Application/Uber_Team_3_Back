package com.reesen.Reesen.service.interfaces;

import com.reesen.Reesen.dto.PassengerDTO;
import com.reesen.Reesen.exceptions.EmailNotConfirmedException;
import com.reesen.Reesen.model.FavoriteRide;
import com.reesen.Reesen.model.Passenger;
import com.reesen.Reesen.model.Ride;
import com.reesen.Reesen.model.VerificationToken;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.Set;

public interface IPassengerService {
	Passenger save(Passenger passenger);
	Optional<Passenger> findOne(Long id);
	Passenger findByEmail(String email);

	PassengerDTO createPassengerDTO(PassengerDTO passengerDTO);

	Page<Passenger> findAll(Pageable page);

	Passenger getPassengerFromPassengerDTO(Long id, PassengerDTO passengerDTO);

 	 void activateAccount(Long passengerId);
  
	boolean getIsEmailConfirmed(String username);

	Set<Passenger> findPassengersByRidesContaining(Ride ride);

    void saveVerificationToken(VerificationToken verificationToken);

	VerificationToken findByUrl(String url);

    Set<FavoriteRide> getFavoriteRides(Long id);
}
