package com.reesen.Reesen.mockup;

import com.reesen.Reesen.dto.VehicleDTO;
import com.reesen.Reesen.model.Location;
import com.reesen.Reesen.model.VehicleType;

public class VehicleMockup {
    private static VehicleDTO vehicleDTO;

    public static VehicleDTO getVehicleDTO(){
        vehicleDTO = new VehicleDTO();
        vehicleDTO.setId(Long.parseLong("123"));
        vehicleDTO.setDriverId(Long.parseLong("123"));
        vehicleDTO.setVehicleType("STANDARDNO");
        vehicleDTO.setModel("VW GOLF 2");
        vehicleDTO.setLicenseNumber("NS 123-AB");
        vehicleDTO.setCurrentLocation(new Location("Kuca Poso", 45.267136, 19.833549));
        vehicleDTO.setPassengerSeats(4);
        vehicleDTO.setBabyTransport(true);
        vehicleDTO.setPetTransport(true);
        return vehicleDTO;
    }
}
