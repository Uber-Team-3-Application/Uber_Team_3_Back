package com.reesen.Reesen.model;

import com.reesen.Reesen.Enums.TypeOfMessage;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Date;

@Entity
public class Message implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = {CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @JoinColumn(name = "senderId")
    private User sender;

    @ManyToOne(cascade = {CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @JoinColumn(name = "receiverId")
    private User receiver;

    @Column(nullable = false)
    private String text;

    @Column
    private Date timeOfSend;

    @Enumerated(EnumType.STRING)
    @Column
    private TypeOfMessage typeOfMessage;

    @Column(nullable = true)
    private Long rideId;

    public Message() {
    }

    public Message(User sender, User receiver, String text, Date timeOfSend, TypeOfMessage typeOfMessage, Long rideId) {
        this.sender = sender;
        this.receiver = receiver;
        this.text = text;
        this.timeOfSend = timeOfSend;
        this.typeOfMessage = typeOfMessage;
        this.rideId = rideId;
    }

    public Message(User sender, User receiver, String text, Date timeOfSend, TypeOfMessage typeOfMessage) {
        this.sender = sender;
        this.receiver = receiver;
        this.text = text;
        this.timeOfSend = timeOfSend;
        this.typeOfMessage = typeOfMessage;

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getTimeOfSend() {
        return timeOfSend;
    }

    public void setTimeOfSend(Date timeOfSend) {
        this.timeOfSend = timeOfSend;
    }

    public TypeOfMessage getTypeOfMessage() {
        return typeOfMessage;
    }

    public void setTypeOfMessage(TypeOfMessage typeOfMessage) {
        this.typeOfMessage = typeOfMessage;
    }

    public Long getRideId() {
        return rideId;
    }

    public void setRideId(Long rideId) {
        this.rideId = rideId;
    }
}
