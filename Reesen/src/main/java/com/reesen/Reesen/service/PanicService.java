package com.reesen.Reesen.service;

import com.reesen.Reesen.dto.*;
import com.reesen.Reesen.model.Location;
import com.reesen.Reesen.model.Panic;
import com.reesen.Reesen.model.User;
import com.reesen.Reesen.repository.PanicRepository;
import com.reesen.Reesen.service.interfaces.IPanicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
public class PanicService implements IPanicService {

    private final PanicRepository panicRepository;

    @Autowired
    public PanicService(PanicRepository panicRepository){
        this.panicRepository = panicRepository;
    }

    @Override
    public List<Panic> findAll(){
        return this.panicRepository.findAll();
    }

}
