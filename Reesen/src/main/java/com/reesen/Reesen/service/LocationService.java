package com.reesen.Reesen.service;

import com.reesen.Reesen.model.Location;
import com.reesen.Reesen.repository.LocationRepository;
import com.reesen.Reesen.service.interfaces.ILocationService;
import org.springframework.beans.factory.annotation.Autowired;

public class LocationService implements ILocationService {
    private final LocationRepository locationRepository;

    @Autowired
    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    @Override
    public Location findOne(Long id) {
        return null;
    }

    @Override
    public Location save(Location location) {
        return null;
    }
}
