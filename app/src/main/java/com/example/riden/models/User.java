package com.example.riden.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("User")
public class User extends ParseObject {
    public static final String USERNAME_KEY = "username";
    public static final String PHONENUMBER_KEY = "phoneNumber";

    public String getUsername() {
        return getString(USERNAME_KEY);
    }
    public void setUsername(String username) {
        put(USERNAME_KEY, username);
    }
    public String getPhoneNumber() {
        return getString(PHONENUMBER_KEY);
    }
    public void setPhoneNumber(String number) {
        put(PHONENUMBER_KEY, number);
    }
}
