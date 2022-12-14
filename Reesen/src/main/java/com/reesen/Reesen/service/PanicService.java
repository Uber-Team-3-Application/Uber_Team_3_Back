package com.reesen.Reesen.service;

import com.reesen.Reesen.dto.*;
import com.reesen.Reesen.model.Location;
import com.reesen.Reesen.model.Panic;
import com.reesen.Reesen.model.User;
import com.reesen.Reesen.repository.PanicRepository;
import com.reesen.Reesen.service.interfaces.IPanicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
public class PanicService implements IPanicService {

    private final PanicRepository panicRepository;

    @Autowired
    public PanicService(PanicRepository panicRepository){
        this.panicRepository = panicRepository;
    }



    @Override
    public List<Panic> findAll(){
        return this.panicRepository.findAll();
    }

    @Override
    public PanicTotalDTO getPanicTotalDTO() {
        PanicTotalDTO panicTotalDTO = new PanicTotalDTO();

        panicTotalDTO.setTotalCount(243);

        PanicDTO panicDTO = new PanicDTO();
        panicDTO.setId(Long.parseLong("10"));
        panicDTO.setTime(Date.from(Instant.now()));
        panicDTO.setReason("Driver is drinking while driving");
        User user = new User(
                "Pera",
                "Peric",
                "U3dhZ2dlciByb2Nrcw==",
                "+381123123",
                "pw.peric@email.com", "pw",
                false, false,
                "Bulevar Oslobodjenja 74");
        panicDTO.setUser(new PanicUserDTO(user));

        PanicRideDTO panicRideDTO = new PanicRideDTO();
        panicRideDTO.setDriver(new UserDTO(
                Long.parseLong("123"),
                "user@example.com"
                ));

        panicRideDTO.addPassenger(new UserDTO(
                Long.parseLong("123"),
                "user@example.com"
                ));
        panicRideDTO.setId(Long.parseLong("123"));

        panicRideDTO.setBabyTransport(true);
        panicRideDTO.setPetTransport(true);
        panicRideDTO.setEstimatedTimeInMinutes(5);
        panicRideDTO.setStartTime(Date.from(Instant.now()));
        panicRideDTO.setEndTime(Date.from(Instant.now()));
        panicRideDTO.setTotalCost(1235);
        panicRideDTO.setVehicleType("STANDARDNO");


        PanicLocationDTO panicLocationDTO = new PanicLocationDTO(new Location(45.267136, 19.833549, "Bulevar oslobodjenja 46"));
        LocationDTO departure = new LocationDTO("Bulevar oslobodjenja 46", 45.267136, 19.833549);
        LocationDTO destination = new LocationDTO("Bulevar oslobodjenja 46", 45.267136, 19.833549);

        panicRideDTO.addLocation(new RouteDTO(departure, destination));
        panicRideDTO.setRejection(
                new DeductionDTO("Ride is canceled due to previous problems with the passenger",
                        Date.from(Instant.now())));
        panicDTO.setRide(panicRideDTO);
        panicTotalDTO.addPanicDTO(panicDTO);
        return panicTotalDTO;
    }
}
