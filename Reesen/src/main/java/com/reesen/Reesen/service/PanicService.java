package com.reesen.Reesen.service;

import com.reesen.Reesen.model.Panic;
import com.reesen.Reesen.repository.PanicRepository;
import com.reesen.Reesen.service.interfaces.IPanicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PanicService implements IPanicService {

    private final PanicRepository panicRepository;

    @Autowired
    public PanicService(PanicRepository panicRepository){
        this.panicRepository = panicRepository;
    }

    @Override
    public List<Panic> getPanicNotifications() {
        return this.panicRepository.findAll();
    }
}
