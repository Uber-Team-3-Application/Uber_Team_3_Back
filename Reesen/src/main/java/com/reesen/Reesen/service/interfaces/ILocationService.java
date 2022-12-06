package com.reesen.Reesen.service.interfaces;

import com.reesen.Reesen.model.Location;

public interface ILocationService {
    Location findOne(Long id);
    Location save(Location location);
}
