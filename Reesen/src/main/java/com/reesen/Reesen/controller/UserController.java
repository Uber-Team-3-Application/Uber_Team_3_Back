package com.reesen.Reesen.controller;

import com.reesen.Reesen.dto.*;
import com.reesen.Reesen.model.*;
import com.reesen.Reesen.model.paginated.Paginated;
import com.reesen.Reesen.service.interfaces.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
            @PathVariable("id") Long id,
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @RequestParam("sort") String sort,
            @RequestParam("from") String from,
            @RequestParam("to") String to
    ) {
        User user = userService.findOne(id);
        Set<RideDTO> rides = new HashSet<>();
        if (driverService.findOne(id) != null) {
            for (Ride ride : ((Driver)user).getRides()) {
                RideDTO rideDTO = new RideDTO(ride);
                rides.add(rideDTO);
            }
        }
        else {
            if (passengerService.findOne(id) != null) {
                for (Ride ride : ((Passenger)user).getRides()) {
                    RideDTO rideDTO = new RideDTO(ride);
                    rides.add(rideDTO);
                }
            }
        }
        // TODO: VRATI MOCKUP

        Paginated<RideDTO> ridePaginated = new Paginated<>(243, rides);
        return new ResponseEntity<>(ridePaginated, HttpStatus.OK);

    }


    @GetMapping()
    public ResponseEntity<Paginated<User>> getUsers(
            @RequestParam("page") int page,
            @RequestParam("size") int size
    ) {

        Paginated<User> userPaginated = new Paginated<>(243);

        Set<User> users = new HashSet<>(); // ovo izbrisati
        // TODO: VRATI MOCKUP
        /// ovo ostaviti:   Set<User> users =  this.userService.getUsers();

        userPaginated.setResults(users);
        return new ResponseEntity<Paginated<User>>(userPaginated, HttpStatus.OK);
    }


    // TODO: TOKENS
    @PostMapping("api/login")
    public ResponseEntity<Void> logIn(@RequestBody UserDTO userDTO) {
        return new ResponseEntity<Void>(HttpStatus.OK);
    }


    @GetMapping("/{id}/message")
    public ResponseEntity<Paginated<MessageDTO>> getUserMessages(
            @PathVariable Long id) {

        User sender = this.userService.findOne(id);
        Set<Message> messages = messageService.getMessagesBySender(sender);
        Set<MessageDTO> retVal = new HashSet<>();
        for (Message message : messages) {
            retVal.add(new MessageDTO(message));
        }
        return new ResponseEntity<>(new Paginated<MessageDTO>(243, retVal), HttpStatus.OK);
    }


    @PostMapping("/{id}/message")
    public ResponseEntity<MessageDTO> sendMessageToTheUser(
            @PathVariable Long id,
            @RequestBody MessageDTO messageDTO
    ) {
        User sender = this.userService.findOne(id);
        User receiver = this.userService.findOne(messageDTO.getReceiverId());

        Message message = new Message(sender, receiver, messageDTO.getMessage(), messageDTO.getTimeOfSending(), messageDTO.getType());
        message = this.messageService.save(message);
        return new ResponseEntity<>(new MessageDTO(message), HttpStatus.CREATED);
    }

    @PutMapping("/{id}/block")
    public ResponseEntity<Void> blockUser(@PathVariable Long id) {
        User user = this.userService.findOne(id);
        user.setBlocked(true);
        userService.save(user);
        return new ResponseEntity<>(HttpStatus.OK);

    }

    @PutMapping("/{id}/unblock")
    public ResponseEntity<Void> unblockUser(@PathVariable Long id) {
        User user = this.userService.findOne(id);
        user.setBlocked(false);
        userService.save(user);
        return new ResponseEntity<>(HttpStatus.OK);

    }

    @PostMapping("/{id}/note")
    public ResponseEntity<RemarkDTO> createNote(
            @PathVariable Long id,
            @RequestBody RemarkDTO remarkDTO
    ) {
        User user = userService.findOne(id);
        Remark remark = new Remark(remarkDTO.getMessage(), user);
        remarkService.save(remark);
        RemarkDTO remarkDto = new RemarkDTO(id, new Date(), remark.getMessage()); // KOJI DATUM SE PROSLEDJUJE ????

        return new ResponseEntity<>(remarkDto, HttpStatus.OK);
    }

    @GetMapping("/{id}/note")
    public ResponseEntity<Paginated<RemarkDTO>> getNotes(
            @PathVariable Long id,
            @RequestParam int page,
            @RequestParam int size
    ) {

        User user = userService.findOne(id);
        Set<Remark> remarks = remarkService.getRemarksByUser(user);
        Set<RemarkDTO> remarksDto = new HashSet<>();
        for (Remark remark : remarks) {
            remarksDto.add(new RemarkDTO(remark));
        }

        Paginated<RemarkDTO> remarksDTO = new Paginated<>(243, remarksDto);
        return new ResponseEntity<>(remarksDTO, HttpStatus.OK);
    }



}