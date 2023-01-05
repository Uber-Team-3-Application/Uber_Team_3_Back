package com.reesen.Reesen.Enums;

public enum TypeOfReport {
    RIDES_PER_DAY,
    KILOMETERS_PER_DAY,
    MONEY_EARNED_PER_DAY,
    MONEY_SPENT_PER_DAY;

    public static TypeOfReport getTypeOfReport(String name){
        if(name.equalsIgnoreCase("RIDES_PER_DAY"))
            return RIDES_PER_DAY;
        if(name.equalsIgnoreCase("KILOMETERS_PER_DAY"))
            return KILOMETERS_PER_DAY;
        if(name.equalsIgnoreCase("MONEY_EARNED_PER_DAY"))
            return MONEY_EARNED_PER_DAY;
        return MONEY_SPENT_PER_DAY;

    }

}
