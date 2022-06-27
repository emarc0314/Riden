package com.example.riden.models;

import com.parse.Parse;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Ride")
public class Ride extends ParseObject {
    public static final String DRIVER_KEY = "driver";
    public static final String SEATS_KEY = "numberSeats";
    public static final String CAR_IMAGE_KEY = "carImage";
    public static final String IS_RESERVED_KEY = "isReserved";

    public ParseUser getDriver() {
        return getParseUser(DRIVER_KEY);
    }

    public int getSeats() {
        try {
            fetchIfNeeded().getString(SEATS_KEY);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return getInt(SEATS_KEY);
    }

    public ParseFile getCarImage() {
        return getParseFile(CAR_IMAGE_KEY);
    }

    public Boolean isReserved() {
        return getBoolean(IS_RESERVED_KEY);
    }
    public void setReserved(Boolean reserved) {
        put(IS_RESERVED_KEY, reserved);
    }
}
