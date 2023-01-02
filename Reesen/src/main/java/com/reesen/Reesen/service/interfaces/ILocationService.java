package com.reesen.Reesen.service.interfaces;

import com.reesen.Reesen.dto.CurrentLocationDTO;
import com.reesen.Reesen.model.Location;

public interface ILocationService {
    Location findOne(Long id);
    Location save(Location location);

    Location getLocation(CurrentLocationDTO locationDTO);

}
