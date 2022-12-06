package com.reesen.Reesen.controller;

import com.reesen.Reesen.dto.*;
import com.reesen.Reesen.model.Ride;
import com.reesen.Reesen.model.User;
import com.reesen.Reesen.service.interfaces.IPanicService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Date;

@CrossOrigin
@RestController
@RequestMapping(value = "api/panic")
public class PanicController {

    private final IPanicService panicService;

    @Autowired
    public PanicController(IPanicService panicService){
        this.panicService = panicService;
    }

    @GetMapping
    public ResponseEntity<PanicTotalDTO> getPanicNotifications(){

        PanicTotalDTO panicTotalDTO = new PanicTotalDTO();
        panicTotalDTO.setTotalCount(243);

        PanicDTO panicDTO = new PanicDTO();
        panicDTO.setId(Long.parseLong("10"));
        panicDTO.setTime(Date.from(Instant.now()));
        panicDTO.setReason("Driver is drinking while driving");

        panicDTO.setUser(new User(
                "Pera",
                "Peric",
                "U3dhZ2dlciByb2Nrcw==",
                "+381123123",
                "pera.peric@email.com",
                "Bulevar Oslobodjenja 74"));

        PanicRideDTO panicRideDTO = new PanicRideDTO();
        panicRideDTO.setDriver(new UserDTO(
                Long.parseLong("123"),
                "user@example.com"));

        panicRideDTO.addPassenger(new UserDTO(
                Long.parseLong("123"),
                "user@example.com"));

        panicRideDTO.setBabyTransport(true);
        panicRideDTO.setPetTransport(true);
        panicRideDTO.setEstimatedTimeInMinutes(5);
        panicRideDTO.setStartTime(Date.from(Instant.now()));
        panicRideDTO.setEndTime(Date.from(Instant.now()));
        panicRideDTO.setTotalCost(1235);

        panicDTO.setRide(panicRideDTO);

        panicTotalDTO.addPanicDTO(panicDTO);

        return new ResponseEntity<>(panicTotalDTO, HttpStatus.OK);

    }

}
