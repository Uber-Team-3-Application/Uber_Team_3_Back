package com.reesen.Reesen.mockup;

import com.reesen.Reesen.dto.CreatedDriverDTO;

public class DriverMockup {

    public static CreatedDriverDTO getDriver(Long id){
        CreatedDriverDTO driver = new CreatedDriverDTO();
        driver.setId(id);
        driver.setName("Pera");
        driver.setSurname("Perić");
        driver.setProfilePicture("U3dhZ2dlciByb2Nrcw==");
        driver.setEmail("pera.peric@email.com");
        driver.setTelephoneNumber("+381123123");
        driver.setAddress("Bulevar Oslobodjenja 74");
        return driver;
    }
}
