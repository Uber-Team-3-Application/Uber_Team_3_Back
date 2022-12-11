package com.reesen.Reesen.mockup;

import com.reesen.Reesen.dto.UserFullDTO;
import com.reesen.Reesen.model.User;

public class UserMockup {

    public static UserFullDTO getUser() {
            UserFullDTO userFullDTO = new UserFullDTO();
            userFullDTO.setId(Long.parseLong("10"));
            userFullDTO.setName("Pera");
            userFullDTO.setSurname("Perić");
            userFullDTO.setProfilePicture("U3dhZ2dlciByb2Nrcw==");
            userFullDTO.setEmail("pera.peric@email.com");
            userFullDTO.setTelephoneNumber("+381123123");
            userFullDTO.setAddress("Bulevar Oslobodjenja 74");
            return userFullDTO;
    }
}
