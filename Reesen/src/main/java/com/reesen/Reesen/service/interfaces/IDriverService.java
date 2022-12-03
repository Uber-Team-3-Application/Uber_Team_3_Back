package com.reesen.Reesen.service.interfaces;

import com.reesen.Reesen.model.Driver;

public interface IDriverService {
    Driver save(Driver driver);
    Driver findOne(Long id);
}
