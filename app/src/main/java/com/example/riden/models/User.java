package com.example.riden.models;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("_User")
public class User extends ParseUser {
    public static final String USERNAME_KEY = "username";
    public static final String PHONENUMBER_KEY = "phoneNumber";
    public static final String FULLNAME_KEY = "fullName";
    public static final String PROFILEPHOTO_KEY = "profilePhoto";
    public static final String CARIMAGE_KEY = "carImage";


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
        return getParseFile(PROFILEPHOTO_KEY);
    }

    public ParseFile getCarImage() {
        return getParseFile(CARIMAGE_KEY);
    }
}
