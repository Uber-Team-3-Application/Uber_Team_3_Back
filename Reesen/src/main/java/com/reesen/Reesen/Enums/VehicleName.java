package com.reesen.Reesen.Enums;

public enum VehicleName {

    STANDARD, LUXURY, VAN;

    public static VehicleName getVehicleName(String name){
        if(name.equalsIgnoreCase("standard") || name.equalsIgnoreCase("standardno"))
            return STANDARD;
        if(name.equalsIgnoreCase("luxury") || name.equalsIgnoreCase("luksuzno"))
            return LUXURY;
        return VAN;
    }

}
