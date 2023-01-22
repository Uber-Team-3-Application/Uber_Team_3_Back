package com.reesen.Reesen.dto;

import com.reesen.Reesen.Enums.VehicleName;
import com.reesen.Reesen.model.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
public class FavoriteRideDTO {

    private Long id;
    private String favoriteName;
    private Set<UserDTO> passengers;
    private boolean babyTransport;
    private boolean petTransport;
    private VehicleTypeDTO vehicleType;
    private Set<RouteDTO> locations;

    public FavoriteRideDTO(Long id, String favoriteName, Set<UserDTO> passengers, boolean babyTransport, boolean petTransport, VehicleTypeDTO vehicleType, Set<RouteDTO> locations) {
        this.id = id;
        this.favoriteName = favoriteName;
        this.passengers = passengers;
        this.babyTransport = babyTransport;
        this.petTransport = petTransport;
        this.vehicleType = vehicleType;
        this.locations = locations;
    }

    public FavoriteRideDTO(FavoriteRide ride) {
        this.id = ride.getId();
        this.favoriteName = ride.getFavoriteName();
        this.babyTransport = ride.isBabyAccessible();
        this.petTransport = ride.isPetAccessible();
        setPassengers(ride);
        setVehicleType(ride);
        setLocations(ride);
    }


    private void setLocations(FavoriteRide ride) {
        locations = new LinkedHashSet<>();
        for (Route route : ride.getLocations()) {
            locations.add(new RouteDTO(route));
        }
    }


    private void setPassengers(FavoriteRide ride) {
        passengers = new HashSet<>();
        for (Passenger passenger : ride.getPassengers()) {
            passengers.add(new UserDTO(passenger.getId(), passenger.getEmail()));
        }
    }

    private void setVehicleType(FavoriteRide ride) {
        if (ride.getVehicleType().getName() == VehicleName.VAN)
            this.vehicleType = VehicleTypeDTO.VAN;
        else if (ride.getVehicleType().getName() == VehicleName.LUXURY)
            this.vehicleType = VehicleTypeDTO.LUXURY;
        else if (ride.getVehicleType().getName() == VehicleName.STANDARD)
            this.vehicleType = VehicleTypeDTO.STANDARD;
    }
}
