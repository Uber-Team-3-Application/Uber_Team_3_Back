package com.reesen.Reesen.controller;

import com.reesen.Reesen.dto.*;
import com.reesen.Reesen.dto.RideDTO;
import com.reesen.Reesen.mockup.*;
import com.reesen.Reesen.model.*;
import com.reesen.Reesen.model.paginated.Paginated;
import com.reesen.Reesen.service.interfaces.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasAnyRole('ADMIN', 'DRIVER', 'PASSENGER')")
    public ResponseEntity<Paginated<RideDTO>> getRide(
            @PathVariable("id") int id,
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @RequestParam("sort") String sort,
            @RequestParam("from") String from,
            @RequestParam("to") String to
    ) {
        User user = userService.findOne((long) id);
        Set<RideDTO> rides = new HashSet<>();
        if (driverService.findOne((long)id) != null) {
            for (Ride ride : ((Driver)user).getRides()) {
                RideDTO rideDTO = new RideDTO(ride);
                rides.add(rideDTO);
            }
        }
        else {
            if (passengerService.findOne((long)id) != null) {
                for (Ride ride : ((Passenger)user).getRides()) {
                    RideDTO rideDTO = new RideDTO(ride);
                    rides.add(rideDTO);
                }
            }
        }

        Paginated<RideDTO> ridePaginated = new Paginated<>(rides.size(), rides);
        return new ResponseEntity<>(ridePaginated, HttpStatus.OK);

    }


    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DRIVER', 'PASSENGER')")
    public ResponseEntity<Paginated<UserFullDTO>> getUsers(
            @RequestParam("page") int page,
            @RequestParam("size") int size
    ) {

        Set<UserFullDTO> users = new HashSet<>();
        for (User user : userService.getUsers()) {
            users.add(new UserFullDTO(user));
        }
        Paginated<UserFullDTO> userPaginated = new Paginated<>(users.size());
        userPaginated.setResults(users);
        return new ResponseEntity<>(userPaginated, HttpStatus.OK);
    }


    @PostMapping("/login")
    //TODO: NIJE IMPLEMENTIRALA LOGIKA TOKENA
    public ResponseEntity<TokenDTO> logIn(@RequestBody LoginDTO login) {
        return new ResponseEntity<>(TokenMockup.getToken(), HttpStatus.OK);
    }


    @GetMapping("/{id}/message")
    @PreAuthorize("hasAnyRole('ADMIN', 'DRIVER', 'PASSENGER')")
    public ResponseEntity<Paginated<MessageFullDTO>> getUserMessages(
            @PathVariable int id) {

        User sender = this.userService.findOne((long) id);
        Set<Message> messages = messageService.getMessagesBySender(sender);
        Set<MessageFullDTO> retVal = new HashSet<>();
        for (Message message : messages) {
            retVal.add(new MessageFullDTO(message));
        }

        return new ResponseEntity<>(new Paginated<>(retVal.size(), retVal), HttpStatus.OK);
    }


    @PostMapping("/{id}/message")
    @PreAuthorize("hasAnyRole('DRIVER', 'PASSENGER')")
    public ResponseEntity<MessageFullDTO> sendMessageToTheUser(
            @PathVariable int id,
            @RequestBody MessageDTO messageDto
    ) {
        User sender = userService.findOne((long)id);
        User receiver = userService.findOne(messageDto.getReceiverId());

        Message message = new Message(sender, receiver, messageDto.getMessage(), Date.from(Instant.now()), messageDto.getType());
        message = messageService.save(message);
        MessageFullDTO messageFullDTO = new MessageFullDTO(message);
        return new ResponseEntity<>(messageFullDTO, HttpStatus.OK);
    }

    @PutMapping("/{id}/block")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> blockUser(@PathVariable int id) {
        User user = this.userService.findOne((long) id);
        user.setBlocked(true);
        userService.save(user);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }

    @PutMapping("/{id}/unblock")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> unblockUser(@PathVariable int id) {
        User user = this.userService.findOne((long) id);
        user.setBlocked(false);
        userService.save(user);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }

    @PostMapping("/{id}/note")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RemarkDTO> createNote(
            @PathVariable int id,
            @RequestBody String message
    ) {
        User user = userService.findOne((long) id);
        Remark remark = new Remark(message, user);
        remark = remarkService.save(remark);
        RemarkDTO remarkDto = new RemarkDTO((long) id, Date.from(Instant.now()), remark.getMessage());
        return new  ResponseEntity<>(remarkDto, HttpStatus.OK);
    }

    @GetMapping("/{id}/note")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Paginated<RemarkDTO>> getNotes(
            @PathVariable int id,
            @RequestParam int page,
            @RequestParam int size
    ) {

User user = userService.findOne((long) id);
        Set<Remark> remarks = remarkService.getRemarksByUser(user);
        Set<RemarkDTO> remarksDto = new HashSet<>();
        for (Remark remark : remarks) {
            RemarkDTO remarkDTO = new RemarkDTO(remark);
            remarkDTO.setDate(Date.from(Instant.now()));
            remarksDto.add(remarkDTO);
        }

        return new ResponseEntity<>(new Paginated<>(remarksDto.size(), remarksDto), HttpStatus.OK);
    }


}