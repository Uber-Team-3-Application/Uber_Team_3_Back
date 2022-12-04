package com.reesen.Reesen.Enums;

public enum VehicleName {

    STANDARD, LUXURY, VAN;

    public static VehicleName getVehicleName(String name){
        if(name.toLowerCase().equals("standard") || name.toLowerCase().equals("standardno"))
            return STANDARD;
        if(name.toLowerCase().equals("luxury") || name.toLowerCase().equals("luksuzno"))
            return LUXURY;
        return VAN;
    }

}
