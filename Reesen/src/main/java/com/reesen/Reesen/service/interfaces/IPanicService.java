package com.reesen.Reesen.service.interfaces;

import com.reesen.Reesen.dto.PanicTotalDTO;
import com.reesen.Reesen.model.Panic;

import java.util.List;

public interface IPanicService {


    List<Panic> findAll();

    Panic findOne(Long id);
}
