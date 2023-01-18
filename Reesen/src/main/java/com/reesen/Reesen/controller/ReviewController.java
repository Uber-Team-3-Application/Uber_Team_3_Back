package com.reesen.Reesen.controller;

import com.reesen.Reesen.dto.ReviewDTO;
import com.reesen.Reesen.dto.ReviewWithPassengerDTO;
import com.reesen.Reesen.dto.RideReviewDTO;
import com.reesen.Reesen.dto.UserDTO;
import com.reesen.Reesen.mockup.ReviewMockup;
import com.reesen.Reesen.model.Driver.Driver;
import com.reesen.Reesen.model.Passenger;
import com.reesen.Reesen.model.Review;
import com.reesen.Reesen.model.Ride;
import com.reesen.Reesen.model.Vehicle;
import com.reesen.Reesen.model.paginated.Paginated;
import com.reesen.Reesen.service.interfaces.*;
import com.reesen.Reesen.validation.UserRequestValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@CrossOrigin
@RestController
@RequestMapping("api/review")
public class ReviewController {

    private final IReviewService reviewService;
    private final IRideService rideService;
    private final IVehicleService vehicleService;
    private final UserRequestValidation userRequestValidation;
    private final IDriverService driverService;
    private final IPassengerService passengerService;


    @Autowired
    public ReviewController(IReviewService reviewService,
                            IRideService rideService,
                            IVehicleService vehicleService,
                            UserRequestValidation userRequestValidation,
                            IPassengerService passengerService,
                            IDriverService driverService
    ) {
        this.reviewService = reviewService;
        this.rideService = rideService;
        this.vehicleService = vehicleService;
        this.userRequestValidation = userRequestValidation;
        this.passengerService = passengerService;
        this.driverService = driverService;
    }

    @PostMapping("/{rideId}/vehicle")
    @PreAuthorize("hasRole('PASSENGER')")
    public ResponseEntity<ReviewWithPassengerDTO> createReviewAboutVehicle(
            @PathVariable int rideId,
            @RequestBody @Valid ReviewDTO review,
            @RequestHeader Map<String, String> headers) {

        Ride ride = this.rideService.findOne((long)rideId);
        if (ride == null)
            return new ResponseEntity("Ride does not exist!", HttpStatus.NOT_FOUND);

        Long passengerId = this.userRequestValidation.getIdFromToken(headers);
        Passenger passenger = this.passengerService.findOne(passengerId).get();

        Review oldReview = this.reviewService.findReviewByPassengerAndRide(passenger, ride).orElse(null);
        if (oldReview != null) {
            oldReview.setVehicleComment(review.getComment());
            oldReview.setPassenger(passenger);
            oldReview.setVehicleRating(review.getRating());
            this.reviewService.save(oldReview);
            ReviewWithPassengerDTO dto = new ReviewWithPassengerDTO(oldReview, false);
            return new ResponseEntity<>(dto, HttpStatus.OK);
        }
        Review newReview = new Review();
        newReview.setVehicleRating(review.getRating());
        newReview.setVehicleComment(review.getComment());
        newReview.setPassenger(passenger);
        newReview.setRide(ride);
        this.reviewService.save(newReview);
        ReviewWithPassengerDTO dto = new ReviewWithPassengerDTO(newReview, false);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }


    @GetMapping("/vehicle/{id}")
    @PreAuthorize("hasAnyRole('PASSENGER', 'DRIVER', 'ADMIN')")
    public ResponseEntity<Paginated<ReviewWithPassengerDTO>> getReviewsForVehicle(@PathVariable int id) {

        Vehicle vehicle = vehicleService.findOne((long) id).orElse(null);

        if (vehicle == null)
            return new ResponseEntity("Vehicle does not exist!", HttpStatus.NOT_FOUND);

        Set<ReviewWithPassengerDTO> retVal = new HashSet<>();
        for (Review review : reviewService.getReviews()) {
            Ride ride = review.getRide();
            Driver driver = this.rideService.findDriverByRideId(ride.getId());
            Vehicle vehicle1 = this.vehicleService.findVehicleByDriverId(driver.getId());
            if (Objects.equals(vehicle1.getId(), vehicle.getId())) {
                retVal.add(new ReviewWithPassengerDTO(review, false));
            }
        }
        return new ResponseEntity<>(new Paginated<>(retVal.size(),retVal), HttpStatus.OK);
    }

    @PostMapping("/{rideId}/driver")
    @PreAuthorize("hasRole('PASSENGER')")
    public ResponseEntity<ReviewWithPassengerDTO> leaveReviewForTheDriver(
            @PathVariable Long rideId,
            @RequestBody @Valid ReviewDTO review,
            @RequestHeader Map<String, String> headers)
    {

        Ride ride = this.rideService.findOne(rideId);
        if (ride == null)
            return new ResponseEntity("Ride does not exist!", HttpStatus.NOT_FOUND);

        Long passengerId = this.userRequestValidation.getIdFromToken(headers);
        Passenger passenger = this.passengerService.findOne(passengerId).get();

        Review oldReview = this.reviewService.findReviewByPassengerAndRide(passenger, ride).orElse(null);
        if (oldReview != null) {
            oldReview.setDriverComment(review.getComment());
            oldReview.setDriverRating(review.getRating());
            oldReview.setPassenger(passenger);
            this.reviewService.save(oldReview);
            ReviewWithPassengerDTO dto = new ReviewWithPassengerDTO(oldReview, true);
            return new ResponseEntity<>(dto, HttpStatus.OK);
        }
        Review newReview = new Review();
        newReview.setDriverRating(review.getRating());
        newReview.setDriverComment(review.getComment());
        newReview.setPassenger(passenger);
        newReview.setRide(ride);
        this.reviewService.save(newReview);
        ReviewWithPassengerDTO dto = new ReviewWithPassengerDTO(newReview, true);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @GetMapping("/driver/{id}")
    @PreAuthorize("hasAnyRole('PASSENGER', 'ADMIN', 'DRIVER')")
    public ResponseEntity<Paginated<ReviewWithPassengerDTO>> getReviewsForTheSpecificDriver(@PathVariable Long id) {

        Driver driver = this.driverService.findOne(id).orElse(null);
        if (driver == null)
            return new ResponseEntity("Driver does not exist!", HttpStatus.NOT_FOUND);

        Set<ReviewWithPassengerDTO> retVal = new HashSet<>();
        for (Review review : this.driverService.getAllReviews(driver.getId())) {
            Long passengerId = review.getPassenger().getId();
            Passenger passenger = passengerService.findOne(passengerId).get();
            String passEmail = passenger.getEmail();
            UserDTO passengerDto= new UserDTO(passengerId, passEmail);
            if (!review.getDriverComment().isEmpty())
                retVal.add(new ReviewWithPassengerDTO(
                        review.getId(), review.getDriverRating(), review.getDriverComment(), passengerDto));
            if (!review.getVehicleComment().isEmpty())
                retVal.add(new ReviewWithPassengerDTO(
                        review.getId(), review.getVehicleRating(), review.getVehicleComment(), passengerDto
                ));

        }
        return new ResponseEntity<>(new Paginated<>(retVal.size(),retVal), HttpStatus.OK);

    }

    @GetMapping("/{rideId}")
    @PreAuthorize("hasAnyRole('PASSENGER', 'ADMIN', 'DRIVER')")
    public  ResponseEntity<HashSet<RideReviewDTO>> getAllReviewsForTheSpecificRide(@PathVariable Long rideId) {

        Ride ride = rideService.findOne(rideId);
        if (ride == null)
            return new ResponseEntity("Ride does not exist!", HttpStatus.NOT_FOUND);

        HashSet<RideReviewDTO> reviews = new HashSet<>();
        for (Review review : reviewService.findReviewsByRide(ride)) {
            review.setPassenger(this.reviewService.findPassengerByReviewId(review.getId()).get());
            ReviewWithPassengerDTO vehicleReview = new ReviewWithPassengerDTO(review, false);
            ReviewWithPassengerDTO driverReview = new ReviewWithPassengerDTO(review, true);
            reviews.add(new RideReviewDTO(vehicleReview, driverReview));
        }
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }


}
