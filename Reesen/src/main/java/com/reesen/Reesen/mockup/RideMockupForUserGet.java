package com.reesen.Reesen.mockup;

import com.reesen.Reesen.dto.*;
import com.reesen.Reesen.dto.RideDTO;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;

public class RideMockupForUserGet {
    public static RideDTO getRide()  {
        RideDTO rideDTO = new RideDTO();
        rideDTO.setId(Long.parseLong("10"));

        LinkedHashSet<RouteDTO> set = new LinkedHashSet<>();
        RouteDTO routeDTO = new RouteDTO();;
        routeDTO.setDeparture(new LocationDTO("Bulevar oslobodjenja 46", 45.267135, 19.833549));
        routeDTO.setDestination(new LocationDTO("Bulevar oslobodjenja 46", 45.267135, 19.833549));
        set.add(routeDTO);
        rideDTO.setLocations(set);

        rideDTO.setStartTime(Date.from(Instant.now()));
        rideDTO.setEndTime(Date.from(Instant.now()));

        rideDTO.setTotalCost(1235);
        rideDTO.setDriver(new UserDTO(Long.parseLong("123"), "user@example.com"));

        HashSet<UserDTO> passengers = new HashSet<>();
        passengers.add(new UserDTO(Long.parseLong("123"), "user@example.com"));
        rideDTO.setPassengers(passengers);
        rideDTO.setEstimatedTimeInMinutes(5);
        rideDTO.setVehicleType(VehicleTypeDTO.STANDARD);
        rideDTO.setBabyTransport(true);
        rideDTO.setPetTransport(true);
        rideDTO.setRejection(new DeductionDTO("Ride is canceled due to previous problems with the passenger", LocalDateTime.now()));

        return rideDTO;
    }

}
