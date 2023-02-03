package com.reesen.Reesen.dto;


import com.reesen.Reesen.model.User;
import lombok.*;

@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserDTO {

    private Long id;
    private String email;

    public UserDTO(User user){
        this.id = user.getId();
        this.email = user.getEmail();
    }
    public UserDTO(String email) {
        this.email = email;

    }


}
