package com.reesen.Reesen.controller;

import com.reesen.Reesen.dto.ReviewDTO;
import com.reesen.Reesen.dto.ReviewWithPassengerDTO;
import com.reesen.Reesen.dto.RideReviewDTO;
import com.reesen.Reesen.mockup.ReviewMockup;
import com.reesen.Reesen.model.paginated.Paginated;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;

@CrossOrigin
@RestController
@RequestMapping("api/review")
public class ReviewController {

    @PostMapping("/{rideId}/vehicle/{id}")
    public ResponseEntity<ReviewWithPassengerDTO> createReviewAboutVehicle(
            @PathVariable int id,
            @PathVariable int rideId,
            @RequestBody ReviewDTO review) {

        return new ResponseEntity<>(ReviewMockup.getReview(), HttpStatus.OK);
    }


    @GetMapping("/vehicle/{id}")
    public ResponseEntity<Paginated<ReviewWithPassengerDTO>> getReviewsForVehicle(@PathVariable int id) {
        HashSet<ReviewWithPassengerDTO> set = new HashSet<>();
        set.add(ReviewMockup.getReview());
        return new ResponseEntity<>(new Paginated<>(243, set), HttpStatus.OK);
    }

    @PostMapping("/{rideId}/driver/{id}")
    public ResponseEntity<ReviewWithPassengerDTO> leaveReviewForTheDriver(
            @PathVariable Long id,
            @PathVariable Long rideId,
            @RequestBody ReviewDTO reviewDTO
    )
    {
        return new ResponseEntity<>(ReviewMockup.getReview(), HttpStatus.OK);
    }

    @GetMapping("/driver/{id}")
    public ResponseEntity<Paginated<ReviewWithPassengerDTO>> getReviewsForTheSpecificDriver(@PathVariable Long id) {
        HashSet<ReviewWithPassengerDTO> set = new HashSet<>();
        set.add(ReviewMockup.getReview());
        return new ResponseEntity<>(new Paginated<>(243, set), HttpStatus.OK);
    }

    @GetMapping("/{rideId}")
    public  ResponseEntity<HashSet<RideReviewDTO>> getAllReviewsForTheSpecificRide(@PathVariable Long rideId) {
        HashSet<RideReviewDTO> reviews = new HashSet<>();
        RideReviewDTO review = new RideReviewDTO(ReviewMockup.getReview(), ReviewMockup.getReview());
        reviews.add(review);
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }


}
