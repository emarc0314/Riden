package com.example.riden;

import android.app.Application;

import com.example.riden.models.Ride;
import com.example.riden.models.User;
import com.parse.Parse;
import com.parse.ParseObject;

import java.util.Map;

public class ParseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ParseObject.registerSubclass(User.class);
        ParseObject.registerSubclass(Ride.class);
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId((getString(R.string.parse_application_id)))
                .clientKey("og3uLay98qgnHYzUlo9ufrCAMd2OdoUOm89ystIi")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}
