package com.tankwars.server.utils;

public class DTO {
    public String message;
    public Boolean success;
    public DTO(String message, Boolean success){
        this.message = message;
        this.success = success;
    }
}
