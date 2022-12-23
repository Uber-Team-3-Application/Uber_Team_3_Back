package com.reesen.Reesen.controller;

import com.reesen.Reesen.dto.*;
import com.reesen.Reesen.model.Panic;
import com.reesen.Reesen.model.paginated.Paginated;
import com.reesen.Reesen.service.interfaces.IPanicService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@CrossOrigin
@RestController
@RequestMapping(value = "api/panic")
public class PanicController {

    private final IPanicService panicService;

    @Autowired
    public PanicController(IPanicService panicService){
        this.panicService = panicService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Paginated<PanicDTO>> getPanicNotifications(){
        List<Panic> panics = this.panicService.findAll();

        Set<PanicDTO> panicDTOs = new HashSet<>();
        for(Panic panic:panics) panicDTOs.add(new PanicDTO(panic));

        return new ResponseEntity<>(new Paginated<PanicDTO>(panicDTOs.size(), panicDTOs), HttpStatus.OK);

    }

}
