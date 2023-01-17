package com.reesen.Reesen.service.interfaces;

import com.reesen.Reesen.dto.CurrentLocationDTO;
import com.reesen.Reesen.model.Location;
import com.reesen.Reesen.model.Route;

import java.util.LinkedHashSet;
import java.util.Set;

public interface ILocationService {
    Location findOne(Long id);
    Location save(Location location);

    Location getLocation(CurrentLocationDTO locationDTO);

    Location getLastLocation(Set<Route> locations);

    Location getFirstLocation(Set<Route> locations);
}
