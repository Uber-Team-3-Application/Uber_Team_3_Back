package com.reesen.Reesen.mockup;

import com.reesen.Reesen.dto.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.HashSet;

public class RideMockupForUserGet {
    public static RideDTO getRide()  {
        RideDTO rideDTO = new RideDTO();
        rideDTO.setId(Long.parseLong("10"));

        HashSet<LocationDTO> set = new HashSet<>();
        set.add(new LocationDTO("Bulevar oslobodjenja 46", 45.267135, 19.833549));
        rideDTO.setLocations(set);

        rideDTO.setStartTime(Date.from(Instant.now()));
        rideDTO.setEndTime(Date.from(Instant.now()));

        rideDTO.setTotalCost(1235);
        rideDTO.setDriver(new UserDTO(Long.parseLong("123"), "user@example.com"));

        HashSet<UserDTO> passengers = new HashSet<>();
        passengers.add(new UserDTO(Long.parseLong("123"), "user@example.com"));
        rideDTO.setPassengers(passengers);
        rideDTO.setEstimatedTimeInMinutes(5);
        rideDTO.setVehicleType(VehicleTypeDTO.STANDARDNO);
        rideDTO.setBabyTransport(true);
        rideDTO.setPetTransport(true);
        rideDTO.setRejection(new DeductionDTO("Ride is canceled due to previous problems with the passenger", Date.from(Instant.now())));

        return rideDTO;
    }

}
