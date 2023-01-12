package com.reesen.Reesen.service.interfaces;

import com.reesen.Reesen.dto.CurrentLocationDTO;
import com.reesen.Reesen.model.Location;
import com.reesen.Reesen.model.Route;

import java.util.LinkedHashSet;

public interface ILocationService {
    Location findOne(Long id);
    Location save(Location location);

    Location getLocation(CurrentLocationDTO locationDTO);

    Location getLastLocation(LinkedHashSet<Route> locations);

    Location getFirstLocation(LinkedHashSet<Route> locations);
}
