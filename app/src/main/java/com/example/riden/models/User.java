package com.example.riden.models;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

@ParseClassName("_User")
public class User extends ParseUser {
    public static final String USERNAME_KEY = "username";
    public static final String PHONENUMBER_KEY = "phoneNumber";
    public static final String FULLNAME_KEY = "fullName";
    public static final String PROFILEPHOTO_KEY = "profilePhoto";
    public static final String CARIMAGE_KEY = "carImage";
    public static final String MYRIDES_KEY = "myRides";
    public static final String MY_OFFERED_RIDES = "myOfferedRides";

    public List<Ride> getMyOfferedRides() {
        return getList(MY_OFFERED_RIDES);
    }

    public void addMyOfferedRide(Ride ride) {
        addUnique(MY_OFFERED_RIDES, ride);
    }

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

    public String getFullName() {
        return getString(FULLNAME_KEY);
    }

    public ParseFile getProfileImage() {
        try {
            fetchIfNeeded().getParseFile(PROFILEPHOTO_KEY);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return getParseFile(PROFILEPHOTO_KEY);
    }

    public ParseFile getCarImage() {
        return getParseFile(CARIMAGE_KEY);
    }

    public List<Ride> getMyRides() {
        List<Ride> rides = getList(MYRIDES_KEY);
        return rides;
    }

    public List<String> getRideObjectIds() {
        //return list of Usernames
        List<Ride> rides = getList(MYRIDES_KEY);
        ArrayList<String> rideObjectId = new ArrayList<>();
        for (Ride ride: rides) {
            rideObjectId.add(ride.getObjectId());
        }
        return rideObjectId;
    }

    public void addRide(Ride ride) {
        addUnique(MYRIDES_KEY, ride);
    }

    public void removeRide(Ride ride) {
        ArrayList<Ride> rideList = new ArrayList<>();
        rideList.add(ride);
        removeAll(MYRIDES_KEY, rideList);
    }
}
