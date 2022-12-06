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
@RequestMapping("api")
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

    @GetMapping("/user/{id}/ride")
    public ResponseEntity<Paginated<RideDTO>> getRide(
            @PathVariable("id") int id,
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @RequestParam("sort") String sort,
            @RequestParam("from") String from,
            @RequestParam("to") String to
    ) {
//        User user = userService.findOne(id);
//        Set<RideDTOWithoutRejection> rides = new HashSet<>();
//        if (driverService.findOne(id) != null) {
//            for (Ride ride : ((Driver)user).getRides()) {
//                RideDTOWithoutRejection rideDTO = new RideDTOWithoutRejection(ride);
//                rides.add(rideDTO);
//            }
//        }
//        else {
//            if (passengerService.findOne(id) != null) {
//                for (Ride ride : ((Passenger)user).getRides()) {
//                    RideDTOWithoutRejection rideDTO = new RideDTOWithoutRejection(ride);
//                    rides.add(rideDTO);
//                }
//            }
//        }
        Set<RideDTO> rides = new HashSet<>();
        rides.add(RideMockupForUserGet.getRide());
        Paginated<RideDTO> ridePaginated = new Paginated<>(243, rides);
        return new ResponseEntity<>(ridePaginated, HttpStatus.OK);

    }


    @GetMapping("/user")
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
        return new ResponseEntity<>(TokenMockup.gwetToken(), HttpStatus.OK);
    }


    @GetMapping("/user/{id}/message")
    public ResponseEntity<Paginated<MessageFullDTO>> getUserMessages(
            @PathVariable int id) {

//        User sender = this.userService.findOne(id);
//        Set<Message> messages = messageService.getMessagesBySender(sender);
//        Set<MessageDTO> retVal = new HashSet<>();
//        for (Message message : messages) {
//            retVal.add(new MessageDTO(message));
//        }
        Set<MessageFullDTO> messages = new HashSet<>();
        messages.add(MessageMockup.getMessage());
        return new ResponseEntity<>(new Paginated<>(243, messages), HttpStatus.OK);
    }


    @PostMapping("/user/{id}/message")
    public ResponseEntity<MessageFullDTO> sendMessageToTheUser(
            @PathVariable int id,
            @RequestBody MessageDTO messageDto
    ) {
//        User sender = this.userService.findOne(id);
//        User receiver = this.userService.findOne(messageDto.getReceiverId());
//
//        Message message = new Message(sender, receiver, messageDto.getMessage(), Date.from(Instant.now()), messageDto.getType());
//        message = this.messageService.save(message);
        return new ResponseEntity<>(MessageMockup.getMessage(), HttpStatus.CREATED);
    }

    @PutMapping("/user/{id}/block")
    public ResponseEntity<Void> blockUser(@PathVariable int id) {
//        User user = this.userService.findOne((long) id);
//        user.setBlocked(true);
//        userService.save(user);
        return new ResponseEntity<>(HttpStatus.OK);

    }

    @PutMapping("/user/{id}/unblock")
    public ResponseEntity<Void> unblockUser(@PathVariable int id) {
//        User user = this.userService.findOne((long) id);
//        user.setBlocked(false);
//        userService.save(user);
        return new ResponseEntity<>(HttpStatus.OK);

    }

    @PostMapping("/user/{id}/note")
    public ResponseEntity<RemarkDTO> createNote(
            @PathVariable int id,
            @RequestBody String message
    ) {
//        User user = userService.findOne(id);
//        Remark remark = new Remark(remarkDTO.getMessage(), user);
//        remarkService.save(remark);
//        RemarkDTO remarkDto = new RemarkDTO(id, Date.from(Instant.now()), remark.getMessage()); // KOJI DATUM SE PROSLEDJUJE ????


        RemarkDTO remarkDto = new RemarkDTO(Long.parseLong("10"), Date.from(Instant.now()), MessageMockup.getMessageInfo());
        return new ResponseEntity<>(remarkDto, HttpStatus.OK);
    }

    @GetMapping("/user/{id}/note")
    public ResponseEntity<Paginated<RemarkDTO>> getNotes(
            @PathVariable int id,
            @RequestParam int page,
            @RequestParam int size
    ) {

//        User user = userService.findOne(id);
//        Set<Remark> remarks = remarkService.getRemarksByUser(user);
//        Set<RemarkDTO> remarksDto = new HashSet<>();
//        for (Remark remark : remarks) {
//            remarksDto.add(new RemarkDTO(remark));
//        }

        HashSet<RemarkDTO> set = new HashSet<>();
        set.add(new RemarkDTO(Long.parseLong("10"), Date.from(Instant.now()), "The passenger has requested and after that aborted the ride"));
        Paginated<RemarkDTO> remarksDTO = new Paginated<>(243, set);
        return new ResponseEntity<>(remarksDTO, HttpStatus.OK);
    }


}