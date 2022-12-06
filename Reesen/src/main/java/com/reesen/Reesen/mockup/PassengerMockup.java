package com.reesen.Reesen.mockup;

import com.reesen.Reesen.dto.Passenger.PassengerDTO;

public class PassengerMockup {
    public static PassengerDTO passenger;
    public static PassengerDTO getPassenger(){
        PassengerDTO passenger = new PassengerDTO();
        passenger.setId(Long.parseLong("10"));
        passenger.setName("Pera");
        passenger.setSurname("Perić");
        passenger.setProfilePicture("U3dhZ2dlciByb2Nrcw==");
        passenger.setEmail("pera.peric@email.com");
        passenger.setTelephoneNumber("+381123123");
        passenger.setAddress("Bulevar Oslobodjenja 74");
        return passenger;
    }
}
