package com.example.riden.models;

import com.parse.Parse;
import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Ride")
public class Ride extends ParseObject {
    public static final String DRIVER_KEY = "driver";
    public static final String SEATS_KEY = "numberSeats";
    public static final String CAR_IMAGE_KEY = "carImage";

    public ParseUser getDriver() {
        return getParseUser(DRIVER_KEY);
    }
    public int getSeats() {
        return getInt(SEATS_KEY);
    }

    public ParseFile getCarImage() {
        return getParseFile(CAR_IMAGE_KEY);
    }
//    public static final String
}
