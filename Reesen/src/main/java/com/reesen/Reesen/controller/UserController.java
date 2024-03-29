package com.reesen.Reesen.controller;

import com.reesen.Reesen.Enums.Role;
import com.reesen.Reesen.dto.*;
import com.reesen.Reesen.model.*;
import com.reesen.Reesen.model.paginated.Paginated;
import com.reesen.Reesen.security.SecurityUser;
import com.reesen.Reesen.security.jwt.JwtTokenUtil;
import com.reesen.Reesen.service.interfaces.*;
import com.reesen.Reesen.validation.UserRequestValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.validation.Valid;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
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
    private final MessageSource messageSource;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    public UserController(IUserService userService, IMessageService messageService, IRemarkService remarkService,
                          IDriverService driverService, IPassengerService passengerService, JavaMailSender mailSender,
                          JwtTokenUtil tokens, IRideService rideService,
                          UserRequestValidation userRequestValidation, MessageSource messageSource) {
        this.userService = userService;
        this.messageService = messageService;
        this.remarkService = remarkService;
        this.driverService = driverService;
        this.passengerService = passengerService;
        this.mailSender = mailSender;
        this.tokens = tokens;
        this.userRequestValidation = userRequestValidation;
        this.rideService = rideService;
        this.messageSource = messageSource;
    }

    @GetMapping("/{id}/ride")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Paginated<PassengerRideDTO>> getRides(
            @PathVariable(value = "id") Long id,
            Pageable page,
            @RequestParam(value = "from", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate from,
            @RequestParam(value = "to", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate to
    ) {

        // -1 none, 0 driver, 1 passengeer
        int userIndicator = -1;
        if (driverService.findOne(id).isPresent()) userIndicator = 0;
        else if(passengerService.findOne(id).isPresent()) userIndicator = 1;
        else return new ResponseEntity("User does not exist!", HttpStatus.NOT_FOUND);

        Date dateFrom = null;
        Date dateTo = null;

        if (from != null || to != null) {
            if  (from != null)
                dateFrom = Date.from(from.atStartOfDay(ZoneId.systemDefault()).toInstant());
            if (to != null)
                dateTo = Date.from(to.atStartOfDay(ZoneId.systemDefault()).toInstant());
        }
        Page<Ride> userRides = null;
        Set<PassengerRideDTO> rides;
        if(userIndicator == 0) {
            userRides = this.rideService.findAllForUserWithRole(id, page, dateFrom, dateTo, Role.DRIVER);
            rides = this.rideService.getFilteredRides(userRides, id);
        }
        else {
            userRides = this.rideService.findAllForUserWithRole(id, page, dateFrom, dateTo, Role.PASSENGER);
            rides = this.rideService.getFilteredRides(userRides, 0L);

        }
        Paginated<PassengerRideDTO> ridePaginated = new Paginated<>(userRides.getNumberOfElements(), rides);
        return new ResponseEntity<>(ridePaginated, HttpStatus.OK);

    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Paginated<UserFullDTO>> getUsers(
            Pageable page
    ) {

        Page<User> users = this.userService.findAll(page);

        Set<UserFullDTO> userDTOS = new HashSet<>();
        for (User user : users) {
            if(user.getRole() != Role.ADMIN)
                userDTOS.add(new UserFullDTO(user));
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
    public ResponseEntity<TokenDTO> logIn(@Valid @RequestBody LoginDTO login) {
        try {

            TokenDTO token = new TokenDTO();
            SecurityUser userDetails = (SecurityUser) this.userService.findByUsername(login.getEmail());

            boolean isEmailConfirmed = this.passengerService.getIsEmailConfirmed(login.getEmail());


            String tokenValue = this.jwtTokenUtil.generateToken(userDetails);
            token.setToken(tokenValue);
            token.setRefreshToken(this.jwtTokenUtil.generateRefreshToken(userDetails));
            Authentication authentication =
                    this.authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(login.getEmail(),
                                    login.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            return new ResponseEntity<>(token, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(new ErrorResponseMessage(
                    this.messageSource.getMessage("user.badCredentials", null, Locale.getDefault())
            ), HttpStatus.BAD_REQUEST);
        }

    }


    @GetMapping("/{id}/message")
    @PreAuthorize("hasAnyRole('ADMIN', 'DRIVER', 'PASSENGER')")
    public ResponseEntity<Paginated<MessageFullDTO>> getUserMessages(
            @PathVariable int id) {

        User user = this.userService.findOne((long) id);

        if (user == null)
            return new ResponseEntity("User does not exist!", HttpStatus.NOT_FOUND);

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

            @PathVariable int id,
            @Valid @RequestBody MessageDTO messageDto,
            @RequestHeader Map<String, String> headers
    ) {
        User receiver = userService.findOne((long)id);

        if (receiver == null)
            return new ResponseEntity("Receiver does not exist!", HttpStatus.NOT_FOUND);

        Integer idOfSender = this.userRequestValidation.getUserId(headers);

        User sender = userService.findOne((long)idOfSender);
        if (sender == null)
            return new ResponseEntity("User does not exist!", HttpStatus.NOT_FOUND);

        if (rideService.findOne((long)messageDto.getRideId()) == null) {
            return new ResponseEntity("Ride does not exist!", HttpStatus.NOT_FOUND);

        }

        Message message = new Message(sender, receiver, messageDto.getMessage(), Date.from(Instant.now()), messageDto.getType(), (long)messageDto.getRideId());
        message = messageService.save(message);
        MessageFullDTO messageFullDTO = new MessageFullDTO(message);
        return new ResponseEntity<>(messageFullDTO, HttpStatus.OK);
    }

    @PutMapping("/{id}/block")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> blockUser(@PathVariable Long id) {
        User user = this.userService.findOne(id);
        if (user == null)
            return new ResponseEntity("User does not exist!", HttpStatus.NOT_FOUND);
        if (user.isBlocked())
            return new ResponseEntity(new ErrorResponseMessage(
                    this.messageSource.getMessage("user.AlreadyBlocked", null, Locale.getDefault())
            ), HttpStatus.BAD_REQUEST);

        user.setBlocked(true);
        userService.save(user);
        return new ResponseEntity("User is successfully blocked",HttpStatus.NO_CONTENT);

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
        if (user == null)
            return new ResponseEntity("User does not exist!", HttpStatus.NOT_FOUND);
        if (!user.isBlocked())
            return new ResponseEntity(new ErrorResponseMessage(
                    this.messageSource.getMessage("user.NotBlocked", null, Locale.getDefault())
            ), HttpStatus.BAD_REQUEST);
        user.setBlocked(false);
        userService.save(user);
        return new ResponseEntity("User is successfully unblocked", HttpStatus.NO_CONTENT);

    }

    @PutMapping("/{id}/changePassword")
    @PreAuthorize("hasAnyRole('DRIVER', 'ADMIN', 'PASSENGER')")
    public ResponseEntity<String> changePassword(
            @Valid @RequestBody ChangePasswordDTO changePasswordDTO,
            @PathVariable Long id) {

        User user = this.userService.findOne(id);
        if(user == null) return new ResponseEntity("User does not exist!", HttpStatus.NOT_FOUND);

        System.out.println(changePasswordDTO.getOldPassword());
        System.out.println(changePasswordDTO.getNewPassword());
        boolean passwordChanged = this.userService.changePassword(changePasswordDTO.getOldPassword(), changePasswordDTO.getNewPassword(), id);
        if(!passwordChanged) return new ResponseEntity(new ErrorResponseMessage(
                this.messageSource.getMessage("user.passwordNotMatching", null, Locale.getDefault())
        ), HttpStatus.BAD_REQUEST);

        return new ResponseEntity("Password successfully changed", HttpStatus.NO_CONTENT);

    }



    @PostMapping("/{id}/note")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RemarkDTO> createNote(
            @PathVariable Long id,
            @Valid @RequestBody NoteDTO note
    ) {
        User user = userService.findOne(id);
        if (user == null)
            return new ResponseEntity("User does not exist!", HttpStatus.NOT_FOUND);

        String message = note.getMessage();
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
        if (user == null)
            return new ResponseEntity("User does not exist!", HttpStatus.NOT_FOUND);

        Set<Remark> remarks = remarkService.getRemarksByUser(user);
        Set<RemarkDTO> remarksDto = new HashSet<>();
        for (Remark remark : remarks) {
            RemarkDTO remarkDTO = new RemarkDTO(remark);
            remarksDto.add(remarkDTO);
        }

        return new ResponseEntity<>(new Paginated<>(remarksDto.size(), remarksDto), HttpStatus.OK);
    }

    @PostMapping("/mail")
    public ResponseEntity<String> sendEmail(@RequestBody EmailDTO email) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(email.getTo());
        helper.setSubject(email.getSubject());
        helper.setText(email.getMessage(),true);
        mailSender.send(message);
        return new ResponseEntity<>("Email sent successfully", HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{id}/resetPassword")
    public ResponseEntity<String> resetPassword(@PathVariable Long id) {

        User user = this.userService.findOne(id);
        if (user == null)
            return new ResponseEntity<>("User does not exist!", HttpStatus.NOT_FOUND);

        ResetPasswordToken resetPasswordToken = new ResetPasswordToken(id);
        this.userService.saveResetPasswordToken(resetPasswordToken);
        System.out.println("\n\n" + resetPasswordToken.toString() + "\n\n");
        System.out.println("\n\n" + resetPasswordToken.getCode() + "\n\n");
        return new ResponseEntity<>(resetPasswordToken.getCode(), HttpStatus.OK);
    }

    @PutMapping("/{id}/resetPassword")
    public ResponseEntity resetPassword(@Valid @RequestBody ResetPasswordDTO resetPasswordDTO,
                                        @PathVariable Long id) {

        User user = this.userService.findOne(id);
        if (user == null)
            return new ResponseEntity<>("User does not exist!", HttpStatus.NOT_FOUND);

        ResetPasswordToken resetPasswordToken = this.userService.findByUserIdAndCode(id, resetPasswordDTO.getCode());

        if(resetPasswordToken == null)
            return new ResponseEntity(new ErrorResponseMessage(
                    this.messageSource.getMessage("user.codeExpired", null, Locale.getDefault())),
                    HttpStatus.BAD_REQUEST);

        if(resetPasswordToken.getExpirationDate().isBefore(LocalDateTime.now()))
            return new ResponseEntity(new ErrorResponseMessage(
                    this.messageSource.getMessage("user.codeExpired", null, Locale.getDefault())),
                    HttpStatus.BAD_REQUEST);

        this.userService.resetPassword(resetPasswordDTO.getNewPassword(), id);
        return new ResponseEntity<>("Password successfully changed!", HttpStatus.NO_CONTENT);
    }

    @GetMapping("/email")
    public ResponseEntity<UserFullDTO> getUserByEmail(@RequestParam("email") String email) {
        return new ResponseEntity<>( new UserFullDTO(this.userService.findByEmail(email).get()), HttpStatus.OK);
    }

    @GetMapping("/{id}/user")
    @PreAuthorize("hasAnyRole('DRIVER', 'ADMIN', 'PASSENGER')")
    public ResponseEntity<UserFullDTO> getUserById(@PathVariable("id") Long id) {
        User user = this.userService.findOne(id);
        UserFullDTO userFullDTO = new UserFullDTO(user);
        return new ResponseEntity<>( userFullDTO, HttpStatus.OK);
    }

    @GetMapping("/admin-user")
    @PreAuthorize("hasAnyRole('DRIVER', 'PASSENGER')")
    public ResponseEntity<Long> getAdminId(){

        Long id = this.userService.getAdminId();
        return new ResponseEntity<>(id, HttpStatus.OK);
    }


}