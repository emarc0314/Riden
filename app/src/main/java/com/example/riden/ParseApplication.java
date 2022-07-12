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
        Map<String,String> mapObj = System.getenv();

        for (Map.Entry<String,String> entry : mapObj.entrySet()) {
            System.out.println("Key: " + entry.getKey()+ " Value: "+ entry.getValue());
        }

        String parseAppId;
        if(System.getenv("parse_id") != null) {
            parseAppId = System.getenv("parse_id");
        }
        else {
            parseAppId = getString(R.string.parse_application_id);
        }

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId((parseAppId))
                .clientKey("og3uLay98qgnHYzUlo9ufrCAMd2OdoUOm89ystIi")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }

}
