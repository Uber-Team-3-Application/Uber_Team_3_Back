package com.reesen.Reesen.controller;

import com.reesen.Reesen.dto.*;
import com.reesen.Reesen.service.interfaces.IPanicService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<PanicTotalDTO> getPanicNotifications(){

        PanicTotalDTO panicTotalDTO = this.panicService.getPanicTotalDTO();
        return new ResponseEntity<>(panicTotalDTO, HttpStatus.OK);

    }

}
