package com.tankwars.server.utils;

import com.tankwars.server.enums.WType;

public class Weight {
    static Integer getWeight(WType weapon){
        switch (weapon){
            case BASIC -> {
                return 10;
            }
            case MISSILE -> {
                return 20;
            }
            case OIL_SLICK -> {
                return 25;
            }
            default -> {
                // must not reach this case
                System.out.println("Something Wrong! must not have reached here");
                return 0;
            }
        }
    }
}
