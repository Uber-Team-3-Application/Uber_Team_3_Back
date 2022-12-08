package com.reesen.Reesen.dto;


import com.reesen.Reesen.Enums.TypeOfMessage;
import com.reesen.Reesen.model.Message;

import java.util.Date;

public class MessageFullDTO {

    private Long id;
    private Date timeOfSending;
    private Long senderId;
    private Long receiverId;
    private String message;
    private TypeOfMessage type;
    private Long rideId;


    public MessageFullDTO(Message message) {
        this.id = message.getId();
        this.timeOfSending = message.getTimeOfSend();
        this.senderId = message.getSender().getId();
        this.receiverId = message.getReceiver().getId();
        this.message = message.getText();
        this.type = message.getTypeOfMessage();
        this.rideId = message.getRideId();

    }

    public MessageFullDTO() {}

    public MessageFullDTO(Long receiverId, String message, TypeOfMessage type, Long rideId) {
        this.receiverId = receiverId;
        this.message = message;
        this.type = type;
        this.rideId = rideId;

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getTimeOfSending() {
        return timeOfSending;
    }

    public void setTimeOfSending(Date timeOfSending) {
        this.timeOfSending = timeOfSending;
    }

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public Long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public TypeOfMessage getType() {
        return type;
    }

    public void setType(TypeOfMessage type) {
        this.type = type;
    }

    public Long getRideId() {
        return rideId;
    }

    public void setRideId(Long rideId) {
        this.rideId = rideId;
    }
}
