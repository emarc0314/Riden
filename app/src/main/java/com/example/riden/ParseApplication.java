package com.example.riden;

import android.app.Application;

import com.example.riden.models.Ride;
import com.example.riden.models.User;
import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ParseObject.registerSubclass(User.class);
        ParseObject.registerSubclass(Ride.class);

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId((getString(R.string.parse_application_id)))
                .clientKey("TgP0awMuSwi0PKUdmW2YizIM2ZZXAoJE6x1KJYjF")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }

}
