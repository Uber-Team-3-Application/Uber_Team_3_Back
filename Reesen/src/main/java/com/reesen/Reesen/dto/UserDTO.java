package com.reesen.Reesen.dto;


import com.reesen.Reesen.model.User;
import lombok.ToString;

@ToString
public class UserDTO {

    private Long id;
    private String email;

    public UserDTO(){
        
    }
    public UserDTO(User user){
        this.id = user.getId();
        this.email = user.getEmail();
    }
    public UserDTO(String email) {
        this.email = email;

    }

    public UserDTO(Long id, String email) {
        this.id = id;
        this.email = email;

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
