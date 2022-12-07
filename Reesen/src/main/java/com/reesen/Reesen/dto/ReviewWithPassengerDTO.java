package com.reesen.Reesen.dto;

public class ReviewWithPassengerDTO {
    private Long id;
    private int rating;
    private String comment;
    private UserDTO passenger;

    public ReviewWithPassengerDTO() {}

    public ReviewWithPassengerDTO(Long id, int rating, String comment, UserDTO passenger) {
        this.id = id;
        this.rating = rating;
        this.comment = comment;
        this.passenger = passenger;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public UserDTO getPassenger() {
        return passenger;
    }

    public void setPassenger(UserDTO passenger) {
        this.passenger = passenger;
    }
}
