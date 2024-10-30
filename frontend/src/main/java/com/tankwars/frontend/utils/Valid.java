package com.tankwars.frontend.utils;

import javafx.scene.control.Alert;

import java.util.regex.Pattern;

public class Valid {
    public boolean emailValid(String email){
        Pattern pattern = Pattern.compile("^[a-zA-Z0-9]+@[a-zA-Z]+\\.[a-zA-Z]{2,6}$");
        return pattern.matcher(email).matches();
    }
}
