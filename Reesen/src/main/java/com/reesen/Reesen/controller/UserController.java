package com.reesen.Reesen.controller;

import com.reesen.Reesen.dto.*;
import com.reesen.Reesen.model.Message;
import com.reesen.Reesen.model.Remark;
import com.reesen.Reesen.model.Ride;
import com.reesen.Reesen.model.User;
import com.reesen.Reesen.service.interfaces.IMessageService;
import com.reesen.Reesen.service.interfaces.IRemarkService;
import com.reesen.Reesen.service.interfaces.IUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("api/user")
public class UserController {
    private final IUserService userService;
    private final IMessageService messageService;
    private final IRemarkService remarkService;

    public UserController(IUserService userService, IMessageService messageService, IRemarkService remarkService) {
        this.userService = userService;
        this.messageService = messageService;
        this.remarkService = remarkService;
    }

    //TODO: IMPLEMENT
    @GetMapping("/{id}/ride")
    public ResponseEntity<Ride> getRide(
            @PathVariable("id") Long userId,
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @RequestParam("sort") String sort,
            @RequestParam("from") String from,
            @RequestParam("to") String to
    ) {
        return new ResponseEntity<>(HttpStatus.OK);
    }


    // TODO: IMPLEMENT
    @GetMapping()
    public ResponseEntity<List<User>> getUsers(
            @RequestParam("page") int page,
            @RequestParam("size") int size
    ) {

        return new ResponseEntity<List<User>>(new ArrayList<>(), HttpStatus.OK);
    }


    // TODO: IMPLEMENT
    @PostMapping("api/login")
    public ResponseEntity<UserDTO> logIn(@RequestBody UserDTO userDTO) {
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @GetMapping("/{id}/message")
    public ResponseEntity<MessagesDTO> getUserMessages(
            @PathVariable Long id,
            @RequestParam("userId") Long userId) {

        User sender = this.userService.findOne(id);
        User receiver = this.userService.findOne(userId);
        List<Message> messages = messageService.findBySenderAndReceiver(sender, receiver);
        List<MessageDTO> retVal = new ArrayList<>();
        for (Message message : messages) {
            retVal.add(new MessageDTO(message));
        }
        return new ResponseEntity<>(new MessagesDTO(retVal.size(), retVal), HttpStatus.OK);
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
        RemarkDTO remarkDto = new RemarkDTO(id, new Date(), remark.getMessage());

        return new ResponseEntity<>(remarkDto, HttpStatus.OK);
    }

    @GetMapping("/{id}/note")
    public ResponseEntity<RemarksDTO> getNotes(
            @PathVariable Long id,
            @RequestParam int page,
            @RequestParam int size
    ) {

        User user = userService.findOne(id);
        List<Remark> remarks = remarkService.getRemarksByUser(user);
        List<RemarkDTO> remarksDto = new ArrayList<>();
        for (Remark remark : remarks) {
            remarksDto.add(new RemarkDTO(remark));
        }
        RemarksDTO remarksDTO = new RemarksDTO(remarks.size(), remarksDto);
        return new ResponseEntity<>(remarksDTO, HttpStatus.OK);
    }
}