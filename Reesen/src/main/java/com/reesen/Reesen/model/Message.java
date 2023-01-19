package com.reesen.Reesen.model;

import com.reesen.Reesen.Enums.TypeOfMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Date;

@Entity
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
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

}
