package com.reesen.Reesen.mockup;

import com.reesen.Reesen.dto.CurrentLocationDTO;
import com.reesen.Reesen.dto.VehicleDTO;
import com.reesen.Reesen.model.Location;

public class VehicleMockup {

    public static VehicleDTO getVehicleDTO(){
        VehicleDTO vehicleDTO = new VehicleDTO();
        vehicleDTO.setId(Long.parseLong("123"));
        vehicleDTO.setDriverId(Long.parseLong("123"));
        vehicleDTO.setVehicleType("STANDARDNO");
        vehicleDTO.setModel("VW GOLF 2");
        vehicleDTO.setLicenseNumber("NS 123-AB");

        Location location = new Location();
        location.setAddress("Bulevar oslobodjenja 46");
        location.setLatitude(45.267136);
        location.setLongitude(19.833549);

        vehicleDTO.setCurrentLocation(new CurrentLocationDTO(location));
        vehicleDTO.setPassengerSeats(4);
        vehicleDTO.setBabyTransport(true);
        vehicleDTO.setPetTransport(true);
        return vehicleDTO;
    }


}
