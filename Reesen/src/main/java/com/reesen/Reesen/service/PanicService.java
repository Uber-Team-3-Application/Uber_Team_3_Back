package com.reesen.Reesen.service;

import com.reesen.Reesen.model.*;
import com.reesen.Reesen.model.Driver.Driver;
import com.reesen.Reesen.repository.*;
import com.reesen.Reesen.service.interfaces.IPanicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class PanicService implements IPanicService {

    private final PanicRepository panicRepository;
    private final UserRepository userRepository;
    private final RideRepository rideRepository;
    private final PassengerRepository passengerRepository;
    private final DriverRepository driverRepository;
    private final DeductionRepository deductionRepository;
    private final ReviewRepository reviewRepository;
    private final RouteRepository routeRepository;
    private final VehicleTypeRepository vehicleTypeRepository;

    @Autowired
    public PanicService(PanicRepository panicRepository, UserRepository userRepository, RideRepository rideRepository, PassengerRepository passengerRepository, DriverRepository driverRepository, DeductionRepository deductionRepository, ReviewRepository reviewRepository, RouteRepository routeRepository, VehicleTypeRepository vehicleTypeRepository){
        this.panicRepository = panicRepository;
        this.userRepository = userRepository;
        this.rideRepository = rideRepository;
        this.passengerRepository = passengerRepository;
        this.driverRepository = driverRepository;
        this.deductionRepository = deductionRepository;
        this.reviewRepository = reviewRepository;
        this.routeRepository = routeRepository;
        this.vehicleTypeRepository = vehicleTypeRepository;
    }

    @Override
    public List<Panic> findAll(){
        List<Panic> panics = this.panicRepository.findAll();
        for(Panic panic: panics){
            setUser(panic);
            setRide(panic);
        }

        return panics;
    }

    private void setUser(Panic panic) {
        Long userId = this.panicRepository.getUserWhoPressedPanic(panic.getId());
        Optional<User> user = this.userRepository.findById(userId);
        if(user.isEmpty()) panic.setUser(null);
        else panic.setUser(user.get());
    }
    private void setRide(Panic panic){
        Long rideId = this.panicRepository.getRideIdFromPanic(panic.getId());
        Optional<Ride> optRide = this.rideRepository.findById(rideId);
        if(optRide.isEmpty()) {panic.setRide(null); return;}
        Ride ride = optRide.get();
        setRideDetails(ride);
        panic.setRide(ride);

    }

    private void setRideDetails(Ride ride) {
        setDriverForPanicRide(ride);
        setPassengersForPanicRide(ride);
        setDeductionForPanicRide(ride);
        setLocationsForPanicRide(ride);
        setVehicleTypeForRide(ride);

        setReviewsForPanicRide(ride);
    }

    private void setReviewsForPanicRide(Ride ride) {
        Set<Review> reviews = this.reviewRepository.findAllByRideId(ride.getId());
        for(Review review:reviews){
            review.setPassenger(this.passengerRepository.findbyReviewId(review.getId()));
        }

        ride.setReview(reviews);
    }

    private void setVehicleTypeForRide(Ride ride) {
        Long vehicleTypeId = this.rideRepository.getVehicleTypeId(ride.getId());
        VehicleType vehicleType = this.vehicleTypeRepository.findById(vehicleTypeId).get();
        ride.setVehicleType(vehicleType);
    }

    private void setLocationsForPanicRide(Ride ride) {
        Set<Route> locations = this.rideRepository.getLocationsByRide(ride.getId());
        for(Route location: locations){
            location.setDestination(this.routeRepository.getDestinationByRoute(location).get());
            location.setDeparture(this.routeRepository.getDepartureByRoute(location).get());
        }
        ride.setLocations(locations);
    }

    private void setDeductionForPanicRide(Ride ride) {
        Optional<Deduction> deduction = this.deductionRepository.findDeductionByRide(ride);
        if(deduction.isEmpty()) ride.setDeduction(new Deduction());
        else ride.setDeduction(deduction.get());
    }

    private void setPassengersForPanicRide(Ride ride) {
        Set<Passenger> passengers = this.passengerRepository.findPassengersByRidesContaining(ride);
        ride.setPassengers(passengers);
    }

    private void setDriverForPanicRide(Ride ride) {
        Long driverId = this.rideRepository.getDriverIdFromRide(ride.getId());
        Optional<Driver> driver = this.driverRepository.findById(driverId);
        if(driver.isEmpty()) ride.setDriver(new Driver());
        else ride.setDriver(driver.get());

    }

}
