package com.reesen.Reesen.service;

import com.reesen.Reesen.Enums.RideStatus;
import com.reesen.Reesen.dto.LocationDTO;
import com.reesen.Reesen.Enums.VehicleName;
import com.reesen.Reesen.dto.VehicleDTO;
import com.reesen.Reesen.dto.VehicleLocationWithAvailabilityDTO;
import com.reesen.Reesen.handlers.RideHandler;
import com.reesen.Reesen.handlers.RideSimulationHandler;
import com.reesen.Reesen.model.*;
import com.reesen.Reesen.model.Driver.Driver;
import com.reesen.Reesen.repository.LocationRepository;
import com.reesen.Reesen.repository.VehicleRepository;
import com.reesen.Reesen.repository.VehicleTypeRepository;
import com.reesen.Reesen.service.interfaces.IRideService;
import com.reesen.Reesen.service.interfaces.IVehicleService;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.socket.WebSocketSession;

import java.util.*;

@Service
public class VehicleService implements IVehicleService {

    private final VehicleRepository vehicleRepository;
    private final VehicleTypeRepository vehicleTypeRepository;
    private final LocationRepository locationRepository;
    private final IRideService rideService;

    @Autowired
    public VehicleService(VehicleRepository vehicleRepository, VehicleTypeRepository vehicleTypeRepository, LocationRepository locationRepository, IRideService rideService){
        this.vehicleRepository = vehicleRepository;
        this.vehicleTypeRepository = vehicleTypeRepository;
        this.locationRepository = locationRepository;
        this.rideService = rideService;
    }

    @Override
    public Optional<Vehicle> findOne(Long id) {
        return this.vehicleRepository.findById(id);
    }

    @Override
    public Vehicle save(Vehicle vehicle) {
        return this.vehicleRepository.save(vehicle);
    }

    public Vehicle createVehicle(VehicleDTO vehicleDTO, Location location){
        Vehicle vehicle = new Vehicle();
        VehicleName vehicleName = VehicleName.getVehicleName(vehicleDTO.getVehicleType());

        vehicle.setType(this.findVehicleTypeByName(vehicleName));
        vehicle.setModel(vehicleDTO.getModel());
        vehicle.setRegistrationPlate(vehicleDTO.getLicenseNumber());
        vehicle.setCurrentLocation(location);
        vehicle.setBabyAccessible(vehicleDTO.isBabyTransport());
        vehicle.setPetAccessible(vehicleDTO.isPetTransport());
        vehicle.setPassengerSeats(vehicleDTO.getPassengerSeats());
        return this.vehicleRepository.save(vehicle);
    }

    @Override
    public Vehicle editVehicle(Vehicle vehicle, VehicleDTO vehicleDTO){

        vehicle.setPassengerSeats(vehicleDTO.getPassengerSeats());
        vehicle.setModel(vehicleDTO.getModel());
        vehicle.setPetAccessible(vehicleDTO.isPetTransport());
        vehicle.setBabyAccessible(vehicleDTO.isBabyTransport());
        vehicle.setRegistrationPlate(vehicleDTO.getLicenseNumber());
        if(vehicleDTO.getVehicleType() != null) {
            VehicleName vehicleName = VehicleName.getVehicleName(vehicleDTO.getVehicleType());
            vehicle.setType(this.findVehicleTypeByName(vehicleName));
        }
        Location location = vehicleRepository.getLocation(vehicle.getId());
        if(location == null){
            location = new Location();
            location.setLatitude(vehicleDTO.getCurrentLocation().getLatitude());
            location.setLongitude(vehicleDTO.getCurrentLocation().getLongitude());
            location.setAddress(vehicleDTO.getCurrentLocation().getAddress());
        }
        location = this.locationRepository.save(location);
        vehicle.setCurrentLocation(location);
        return vehicle;

    }

    public VehicleType findVehicleTypeByName(VehicleName name){
        return this.vehicleTypeRepository.findByName(name);
    }

    @Override
    public VehicleType findType(Long id){
        return this.vehicleRepository.getVehicleType(id);
    }

    @Override
    public Location findLocation(Long id){
        return this.vehicleRepository.getLocation(id);
    }


    @Override
    public Vehicle setCurrentLocation(Vehicle vehicle, LocationDTO locationDTO) {
        Location location = new Location(locationDTO.getLatitude(), locationDTO.getLongitude(), locationDTO.getAddress());
        location = this.locationRepository.save(location);
        vehicle.setCurrentLocation(location);
        return vehicle;
    }

    @Override
    public List<LocationDTO> getAllLocations() {
        List<Long> vehicle_ids = this.vehicleRepository.getAllLocationIds();

        List<LocationDTO> locations = new ArrayList<>();
        for(Long id: vehicle_ids){
            locations.add(new LocationDTO(this.vehicleRepository.getLocation(id)));
        }
        return locations;
    }

    @Override
    public List<VehicleLocationWithAvailabilityDTO> getAllLocationsWithAvailability() {
        return this.vehicleRepository.getAllLocationsWithAvailability();
    }

    @Override
    public LocationDTO getCurrentLocation(Long vehicleId) {
        Location location = this.vehicleRepository.getLocation(vehicleId);
        return new LocationDTO(location);
    }

    @Override
    public Vehicle findVehicleByDriverId(Long id) {
        return this.vehicleRepository.findVehicleByDriverId(id);
    }

    @Override
    public void simulateVehicleByRideId(Long rideId) {
        Ride ride = this.rideService.findOne(rideId);
        Vehicle vehicle = this.vehicleRepository.findVehicleByDriverId(ride.getDriver().getId());
        vehicle.setCurrentLocation(this.locationRepository.findById(vehicle.getCurrentLocation().getId()).get());
        String start = "", end = "";
        if(ride.getStatus() == RideStatus.ACCEPTED){
            start = vehicle.getCurrentLocation().getLongitude() + "," + vehicle.getCurrentLocation().getLatitude();
            for(Route route : ride.getLocations()){
                end = route.getDeparture().getLongitude() + "," + route.getDeparture().getLatitude();
                break;
            }
        }else if(ride.getStatus() == RideStatus.ACTIVE){
            for(Route route : ride.getLocations()){
                start = route.getDeparture().getLongitude() + "," + route.getDeparture().getLatitude();
                end = route.getDestination().getLongitude() + "," + route.getDestination().getLatitude();
                break;
            }
        }else return;

        List<LocationDTO> route = this.getRouteFromOpenRoute(start, end);

        Timer timer = new Timer();

        timer.scheduleAtFixedRate(new TimerTask() {
            int totalPoints = 0;
            @Override
            public void run() {
                if(totalPoints < route.size()){
                    Location location = vehicle.getCurrentLocation();
                    saveLocationForCurrentRide(location, route, totalPoints, vehicle);

                    List<WebSocketSession> sessions = new ArrayList<>();
                    addPassengerSession(sessions, ride);
                    WebSocketSession driverSession = RideHandler.driverSessions.get(ride.getDriver().getId().toString());
                    if(driverSession != null) sessions.add(driverSession);
                    WebSocketSession adminSession = RideHandler.adminSessions.get(Long.toString(6));
                    if(adminSession != null) sessions.add(adminSession);
                    RideSimulationHandler.notifyUsersAboutVehicleLocations(sessions, location);
                    totalPoints++;

                }else timer.cancel();
            }
        }, 1000, 4000);
    }

    private void saveLocationForCurrentRide(Location location, List<LocationDTO> route, int totalPoints, Vehicle vehicle) {
        location.setLongitude(route.get(totalPoints).getLongitude());
        location.setLatitude(route.get(totalPoints).getLatitude());
        location = locationRepository.save(location);
        vehicle.setCurrentLocation(location);
        vehicleRepository.save(vehicle);
    }

    private void addPassengerSession(List<WebSocketSession> sessions, Ride ride) {
        for(Passenger passenger: ride.getPassengers()){
            WebSocketSession passengerSession = RideHandler.passengerSessions.get(passenger.getId().toString());
            if(passengerSession != null) sessions.add(passengerSession);
        }
    }

    @Override
    public List<LocationDTO> getRouteFromOpenRoute(String start, String end) {
        String base = "https://api.openrouteservice.org/v2/directions/driving-car";
        String key = "5b3ce3597851110001cf6248e686109bcf5e46dfa129805f14ec1f16";
        String url = base + "?api_key=" + key + "&start="+start+"&end="+end;

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();

        HttpEntity<String> request = new HttpEntity<>(headers);



        ResponseEntity<String> response = restTemplate.exchange(
                url, HttpMethod.GET, request, String.class);

        String responseString = response.getBody();
        JSONObject json = new JSONObject(responseString);
        JSONArray features = json.getJSONArray("features");
        JSONObject ff = features.getJSONObject(0);
        JSONObject geometry = ff.getJSONObject("geometry");
        JSONArray coords = geometry.getJSONArray("coordinates");
        List<LocationDTO> route = new ArrayList<>();
        for (int i = 0; i < coords.length(); i++) {
            JSONArray coord = coords.getJSONArray(i);
            double longitude = coord.getDouble(0);
            double latitude = coord.getDouble(1);

            LocationDTO location = new LocationDTO();
            location.setLongitude(longitude);
            location.setLatitude(latitude);
            route.add(location);
        }
        return route;
    }

    @Override
    public Vehicle createVehicle(VehicleDTO vehicleDTO, Driver driver){
        Location location = new Location();
        location.setLongitude(vehicleDTO.getCurrentLocation().getLongitude());
        location.setLatitude(vehicleDTO.getCurrentLocation().getLatitude());
        location.setAddress(vehicleDTO.getCurrentLocation().getAddress());
        location = this.locationRepository.save(location);
        Vehicle vehicle = new Vehicle();
        vehicle.setType(this.findVehicleTypeByName(VehicleName.getVehicleName(vehicleDTO.getVehicleType())));
        vehicle.setPassengerSeats(vehicleDTO.getPassengerSeats());
        vehicle.setModel(vehicleDTO.getModel());
        vehicle.setPetAccessible(vehicleDTO.isPetTransport());
        vehicle.setBabyAccessible(vehicleDTO.isBabyTransport());
        vehicle.setRegistrationPlate(vehicleDTO.getLicenseNumber());
        vehicle.setCurrentLocation(location);
        vehicle.setDriver(driver);

        return vehicle;
    }



    public List<VehicleType> getVehicleTypes(){
        return this.vehicleTypeRepository.findAll();
    }
}
