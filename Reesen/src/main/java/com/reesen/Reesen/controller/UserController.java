package com.reesen.Reesen.controller;

import com.reesen.Reesen.dto.*;
import com.reesen.Reesen.dto.Ride.RideDTO;
import com.reesen.Reesen.mockup.*;
import com.reesen.Reesen.model.*;
import com.reesen.Reesen.model.paginated.Paginated;
import com.reesen.Reesen.service.interfaces.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.*;

@CrossOrigin
@RestController
@RequestMapping("api/user")
public class UserController {
    private final IUserService userService;
    private final IMessageService messageService;
    private final IRemarkService remarkService;
    private final IDriverService driverService;
    private final IPassengerService passengerService;

    @Autowired
    public UserController(IUserService userService, IMessageService messageService, IRemarkService remarkService,
                          IDriverService driverService, IPassengerService passengerService) {
        this.userService = userService;
        this.messageService = messageService;
        this.remarkService = remarkService;
        this.driverService = driverService;
        this.passengerService = passengerService;

    }

    @GetMapping("/{id}/ride")
    public ResponseEntity<Paginated<RideDTO>> getRide(
            @PathVariable("id") int id,
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @RequestParam("sort") String sort,
            @RequestParam("from") String from,
            @RequestParam("to") String to
    ) {
        Set<RideDTO> rides = new HashSet<>();
        rides.add(RideMockupForUserGet.getRide());
        Paginated<RideDTO> ridePaginated = new Paginated<>(243, rides);
        return new ResponseEntity<>(ridePaginated, HttpStatus.OK);

    }


    @GetMapping
    public ResponseEntity<Paginated<UserFullDTO>> getUsers(
            @RequestParam("page") int page,
            @RequestParam("size") int size
    ) {

        Paginated<UserFullDTO> userPaginated = new Paginated<>(243);
        Set<UserFullDTO> users = new HashSet<>();
        users.add(UserMockup.getUser());

        userPaginated.setResults(users);
        return new ResponseEntity<>(userPaginated, HttpStatus.OK);
    }


    @PostMapping("/login")
    public ResponseEntity<TokenDTO> logIn(@RequestBody LoginDTO login) {
        return new ResponseEntity<>(TokenMockup.getToken(), HttpStatus.OK);
    }


    @GetMapping("/{id}/message")
    public ResponseEntity<Paginated<MessageFullDTO>> getUserMessages(
            @PathVariable int id) {

        Set<MessageFullDTO> messages = new HashSet<>();
        messages.add(MessageMockup.getMessage());
        return new ResponseEntity<>(new Paginated<>(243, messages), HttpStatus.OK);
    }


    @PostMapping("/{id}/message")
    public ResponseEntity<MessageFullDTO> sendMessageToTheUser(
            @PathVariable int id,
            @RequestBody MessageDTO messageDto
    ) {
        return new ResponseEntity<>(MessageMockup.getMessage(), HttpStatus.OK);
    }

    @PutMapping("/{id}/block")
    public ResponseEntity<Void> blockUser(@PathVariable int id) {
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }

    @PutMapping("/{id}/unblock")
    public ResponseEntity<Void> unblockUser(@PathVariable int id) {
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }

    @PostMapping("/{id}/note")
    public ResponseEntity<RemarkDTO> createNote(
            @PathVariable int id,
            @RequestBody String message
    ) {

        RemarkDTO remarkDto = new RemarkDTO(Long.parseLong("10"), Date.from(Instant.now()), MessageMockup.getMessageInfo());
        return new ResponseEntity<>(remarkDto, HttpStatus.OK);
    }

    @GetMapping("/{id}/note")
    public ResponseEntity<Paginated<RemarkDTO>> getNotes(
            @PathVariable int id,
            @RequestParam int page,
            @RequestParam int size
    ) {

        HashSet<RemarkDTO> set = new HashSet<>();
        set.add(new RemarkDTO(Long.parseLong("10"), Date.from(Instant.now()), "The passenger has requested and after that aborted the ride"));
        Paginated<RemarkDTO> remarksDTO = new Paginated<>(243, set);
        return new ResponseEntity<>(remarksDTO, HttpStatus.OK);
    }


}