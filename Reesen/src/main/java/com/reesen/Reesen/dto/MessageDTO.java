package com.reesen.Reesen.dto;

import com.reesen.Reesen.Enums.TypeOfMessage;

public class MessageDTO {
    private Long receiverId;
    private String message;
    private TypeOfMessage type;
    private Long rideId;

    public MessageDTO() {
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
