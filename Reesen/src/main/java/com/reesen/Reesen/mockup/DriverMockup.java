package com.reesen.Reesen.mockup;

import com.reesen.Reesen.dto.DriverDTO;
import com.reesen.Reesen.model.Driver;

public class DriverMockup {
    public static DriverDTO driver;

    public static DriverDTO getDriver(){
        DriverDTO driver = new DriverDTO();
        driver.setId(Long.parseLong("10"));
        driver.setName("Pera");
        driver.setSurname("Perić");
        driver.setProfilePicture("U3dhZ2dlciByb2Nrcw==");
        driver.setEmail("pera.peric@email.com");
        driver.setTelephoneNumber("+381123123");
        driver.setAddress("Bulevar Oslobodjenja 74");
        return driver;
    }
}
