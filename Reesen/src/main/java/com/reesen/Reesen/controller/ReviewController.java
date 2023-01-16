package com.reesen.Reesen.controller;

import com.reesen.Reesen.dto.ReviewDTO;
import com.reesen.Reesen.dto.ReviewWithPassengerDTO;
import com.reesen.Reesen.dto.RideReviewDTO;
import com.reesen.Reesen.mockup.ReviewMockup;
import com.reesen.Reesen.model.Review;
import com.reesen.Reesen.model.Ride;
import com.reesen.Reesen.model.Vehicle;
import com.reesen.Reesen.model.paginated.Paginated;
import com.reesen.Reesen.service.interfaces.IReviewService;
import com.reesen.Reesen.service.interfaces.IRideService;
import com.reesen.Reesen.service.interfaces.IVehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@CrossOrigin
@RestController
@RequestMapping("api/review")
public class ReviewController {

    private final IReviewService reviewService;
    private final IRideService rideService;
    private final IVehicleService vehicleService;

    @Autowired
    public ReviewController(IReviewService reviewService, IRideService rideService, IVehicleService vehicleService) {
        this.reviewService = reviewService;
        this.rideService = rideService;
        this.vehicleService = vehicleService;
    }

    @PostMapping("/{rideId}/vehicle/{id}")
    @PreAuthorize("hasRole('PASSENGER')")
    public ResponseEntity<ReviewWithPassengerDTO> createReviewAboutVehicle(
            @PathVariable int id,
            @PathVariable int rideId,
            @RequestBody ReviewDTO review) {


        return new ResponseEntity<>(ReviewMockup.getReview(), HttpStatus.OK);
    }


    @GetMapping("/vehicle/{id}")
    @PreAuthorize("hasAnyRole('PASSENGER', 'DRIVER', 'ADMIN')")
    public ResponseEntity<Paginated<ReviewWithPassengerDTO>> getReviewsForVehicle(@PathVariable int id) {

        Vehicle vehicle = vehicleService.findOne((long) id).get();
        Set<ReviewWithPassengerDTO> retVal = new HashSet<>();
        for (Review review : reviewService.getReviews()) {
            if (Objects.equals(review.getRide().getDriver().getVehicle().getId(), vehicle.getId())) {
                retVal.add(new ReviewWithPassengerDTO(review, false));
            }
        }
        return new ResponseEntity<>(new Paginated<>(retVal.size(),retVal), HttpStatus.OK);
    }

    @PostMapping("/{rideId}/driver/{id}")
    @PreAuthorize("hasRole('PASSENGER')")
    public ResponseEntity<ReviewWithPassengerDTO> leaveReviewForTheDriver(
            @PathVariable Long id,
            @PathVariable Long rideId,
            @RequestBody ReviewDTO reviewDTO
    )
    {

        // TODO: PASSENGER ?? --> NIJE IMPLEMENTIRANA LOGIKA TOKENA
        return new ResponseEntity<>(ReviewMockup.getReview(), HttpStatus.OK);
    }

    @GetMapping("/driver/{id}")
    @PreAuthorize("hasAnyRole('PASSENGER', 'ADMIN', 'DRIVER')")
    public ResponseEntity<Paginated<ReviewWithPassengerDTO>> getReviewsForTheSpecificDriver(@PathVariable Long id) {

        Ride ride = rideService.findOne(id);
        Set<ReviewWithPassengerDTO> retVal = new HashSet<>();
        for (Review review : reviewService.getReviews()) {
            if (Objects.equals(review.getRide().getId(), ride.getId())) {
                retVal.add(new ReviewWithPassengerDTO(review, true));
            }
        }
        return new ResponseEntity<>(new Paginated<>(retVal.size(),retVal), HttpStatus.OK);

    }

    @GetMapping("/{rideId}")
    @PreAuthorize("hasAnyRole('PASSENGER', 'ADMIN', 'DRIVER')")
    public  ResponseEntity<HashSet<RideReviewDTO>> getAllReviewsForTheSpecificRide(@PathVariable Long rideId) {

        Ride ride = rideService.findOne(rideId);
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
