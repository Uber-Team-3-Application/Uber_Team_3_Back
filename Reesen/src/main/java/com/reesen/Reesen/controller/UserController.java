package com.reesen.Reesen.controller;

import com.reesen.Reesen.Enums.Role;
import com.reesen.Reesen.dto.*;
import com.reesen.Reesen.exceptions.BadRequestException;
import com.reesen.Reesen.model.*;
import com.reesen.Reesen.model.paginated.Paginated;
import com.reesen.Reesen.security.SecurityUser;
import com.reesen.Reesen.security.jwt.JwtTokenUtil;
import com.reesen.Reesen.service.interfaces.*;
import com.reesen.Reesen.validation.UserRequestValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    private final JavaMailSender mailSender;
    private final JwtTokenUtil tokens;
    private final UserRequestValidation userRequestValidation;

    private final IRideService rideService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    public UserController(IUserService userService, IMessageService messageService, IRemarkService remarkService,
                          IDriverService driverService, IPassengerService passengerService, JavaMailSender mailSender, JwtTokenUtil tokens, UserRequestValidation userRequestValidation, IRideService rideService) {
        this.userService = userService;
        this.messageService = messageService;
        this.remarkService = remarkService;
        this.driverService = driverService;
        this.passengerService = passengerService;
        this.mailSender = mailSender;
        this.tokens = tokens;
        this.userRequestValidation = userRequestValidation;
        this.rideService = rideService;
    }

    @GetMapping("/{id}/ride")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Paginated<UserRidesDTO>> getRides(
            @PathVariable("id") Long id,
            Pageable page,
            @RequestParam(value = "from", required = false) String from,
            @RequestParam(value = "to", required = false) String to
    ) {

        // -1 none, 0 driver, 1 passengeer
        int userIndicator = -1;
        if (driverService.findOne(id).isPresent()) userIndicator = 0;
        else if(passengerService.findOne(id).isPresent()) userIndicator = 1;
        else return new ResponseEntity("No user with given id.", HttpStatus.NOT_FOUND);

        Date dateFrom = null;
        Date dateTo = null;

        if (from != null || to != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            if  (from != null)
                dateFrom = java.sql.Timestamp.valueOf(LocalDateTime.parse(from, formatter));
            if (to != null)
                dateTo = java.sql.Timestamp.valueOf(LocalDateTime.parse(to, formatter));
        }
        Page<Ride> userRides = null;
        if(userIndicator == 0)
            userRides = this.rideService.findAllForUserWithRole(id, page, dateFrom, dateTo, Role.DRIVER);
        else
            userRides = this.rideService.findAllForUserWithRole(id, page, dateFrom, dateTo, Role.PASSENGER);

        Set<UserRidesDTO> rides = new LinkedHashSet<>();
        rides = this.rideService.getFilteredRides(userRides, id);

        Paginated<UserRidesDTO> ridePaginated = new Paginated<>(userRides.getNumberOfElements(), rides);
        return new ResponseEntity<>(ridePaginated, HttpStatus.OK);

    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Paginated<UserTabularDTO>> getUsers(
            Pageable page
    ) {

        Page<User> users = this.userService.findAll(page);

        Set<UserTabularDTO> userDTOS = new HashSet<>();
        for (User user : users) {
            if(user.getRole() != Role.ADMIN)
                userDTOS.add(new UserTabularDTO(user));
        }

        return new ResponseEntity<>(
                new Paginated<>(users.getNumberOfElements(), userDTOS), HttpStatus.OK);
    }
    @GetMapping("/number-of-users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Integer> getTotalNumberOfUsers(){

        Integer totalNumber = this.userService.getTotalNumberOfUsers();
        return new ResponseEntity<>(totalNumber, HttpStatus.OK);
    }
    @GetMapping("/{id}/number-of-rides")
    @PreAuthorize("hasAnyRole('ADMIN', 'DRIVER', 'PASSENGER')")
    public ResponseEntity<Integer> getTotalNumberOfRides(@PathVariable("id") Long id){
        User user = this.userService.findOne(id);
        if(user == null) return new ResponseEntity("Invalid ID.", HttpStatus.NOT_FOUND);

        int totalNumberOfRides = this.userService.getTotalNumberOfRides(user);
        return new ResponseEntity<>(totalNumberOfRides, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenDTO> logIn(@RequestBody LoginDTO login) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (!(auth instanceof AnonymousAuthenticationToken)) {
            throw new BadRequestException("Unauthorized!");
        }

        try {
            TokenDTO token = new TokenDTO();
            SecurityUser userDetails = (SecurityUser) this.userService.findByUsername(login.getEmail());

            boolean isEmailConfirmed = this.passengerService.getIsEmailConfirmed(login.getEmail());
            if(!isEmailConfirmed){
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            String tokenValue = this.jwtTokenUtil.generateToken(userDetails);
            token.setToken(tokenValue);
            token.setRefreshToken(this.jwtTokenUtil.generateRefreshToken(userDetails));
            Authentication authentication =
                    this.authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(login.getEmail(),
                                    login.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            return new ResponseEntity<>(token, HttpStatus.OK);
        } catch (BadCredentialsException e) {
            throw new BadRequestException("Wrong password!");
        }

    }


    @GetMapping("/{id}/message")
    @PreAuthorize("hasAnyRole('ADMIN', 'DRIVER', 'PASSENGER')")
    public ResponseEntity<Paginated<MessageFullDTO>> getUserMessages(
            @PathVariable int id) {

        User user = this.userService.findOne((long) id);
        Set<Message> messages = messageService.getAll(user);
        Set<MessageFullDTO> retVal = new LinkedHashSet<>();
        for (Message message : messages) {
            retVal.add(new MessageFullDTO(message));
        }

        return new ResponseEntity<>(new Paginated<>(retVal.size(), retVal), HttpStatus.OK);
    }


    @PostMapping("/{id}/message")
    @PreAuthorize("hasAnyRole('DRIVER', 'PASSENGER', 'ADMIN')")
    public ResponseEntity<MessageFullDTO> sendMessageToTheUser(
            @PathVariable Long id,
            @RequestBody MessageDTO messageDto,
            @RequestHeader Map<String, String> headers
    ) {
        Long senderId = this.userRequestValidation.getIdFromToken(headers);
        User receiver = userService.findOne(id);
        User sender = userService.findOne(senderId);

        Message message = new Message(sender, receiver, messageDto.getMessage(), Date.from(Instant.now()), messageDto.getType(), messageDto.getRideId());
        message = messageService.save(message);
        MessageFullDTO messageFullDTO = new MessageFullDTO(message);
        return new ResponseEntity<>(messageFullDTO, HttpStatus.OK);
    }

    @PutMapping("/{id}/block")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> blockUser(@PathVariable Long id) {
        User user = this.userService.findOne(id);
        System.out.println(id);
        user.setBlocked(true);
        userService.save(user);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }

    @GetMapping("/{id}/is-blocked")
    @PreAuthorize("hasAnyRole('ADMIN','DRIVER', 'PASSENGER')")
    public ResponseEntity<Boolean> isUserBlocked(@PathVariable Long id){
        User user = this.userService.findOne(id);
        if(user == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        boolean isBlocked = this.userService.getIsUserBlocked(id);
        return new ResponseEntity<>(isBlocked, HttpStatus.OK);
    }

    @PutMapping("/{id}/unblock")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> unblockUser(@PathVariable int id) {
        User user = this.userService.findOne((long) id);
        user.setBlocked(false);
        userService.save(user);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }

    @PutMapping("/{id}/changePassword")
    @PreAuthorize("hasAnyRole('DRIVER', 'ADMIN', 'PASSENGER')")
    public ResponseEntity<String> changePassword(
            @RequestBody ChangePasswordDTO changePasswordDTO,
            @PathVariable Long id) {
        User user = this.userService.findOne(id);
        if(user == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        boolean passwordChanged = this.userService.changePassword(changePasswordDTO.getOld_password(), changePasswordDTO.getNew_password(), id);
        if(!passwordChanged) return new ResponseEntity<>("Current password is not matching", HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>("Password successfully changed", HttpStatus.NO_CONTENT);

    }



    @PostMapping("/{id}/note")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RemarkDTO> createNote(
            @PathVariable Long id,
            @RequestBody String message
    ) {
        User user = userService.findOne(id);
        Remark remark = new Remark(message,  Date.from(Instant.now()), user);
        System.out.println(remark.getMessage());
        remark = remarkService.save(remark);
        RemarkDTO remarkDto = new RemarkDTO(remark);
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
            remarksDto.add(remarkDTO);
        }

        return new ResponseEntity<>(new Paginated<>(remarksDto.size(), remarksDto), HttpStatus.OK);
    }

    @PostMapping("/mail")
    public ResponseEntity<?> sendEmail(@RequestBody EmailDTO email) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(email.getTo());
        helper.setSubject(email.getSubject());
        helper.setText(email.getMessage(),true);
        mailSender.send(message);
        return new ResponseEntity<>("Email sent successfuly", HttpStatus.OK);
    }
    @GetMapping("/{id}/resetPassword")
    public ResponseEntity<String> resetPassword(@PathVariable Long id){
        ResetPasswordToken resetPasswordToken = new ResetPasswordToken(id);
        this.userService.saveResetPasswordToken(resetPasswordToken);
        return new ResponseEntity<>(resetPasswordToken.getCode(), HttpStatus.OK);    }

    @PutMapping("/{id}/resetPassword")
    public ResponseEntity resetPassword(@RequestBody ResetPasswordDTO resetPasswordDTO, @PathVariable Long id) {
        ResetPasswordToken resetPasswordToken = this.userService.findByUserIdAndCode(id, resetPasswordDTO.getCode());
        if(resetPasswordToken == null)
            return new ResponseEntity<>("Wrong code!", HttpStatus.BAD_REQUEST);
        if(resetPasswordToken.getExpirationDate().isBefore(LocalDateTime.now()))
            return new ResponseEntity<>("Reset token expired!", HttpStatus.BAD_REQUEST);
        this.userService.resetPassword(resetPasswordDTO.getPassword(), id);
        return new ResponseEntity<>("Password successfully changed", HttpStatus.NO_CONTENT);
    }

    @GetMapping("/email")
    public ResponseEntity<User> getUserByEmail(@RequestParam String email) {
        return new ResponseEntity<>( this.userService.findByEmail(email).get(), HttpStatus.OK);
    }

}