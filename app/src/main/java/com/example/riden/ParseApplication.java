package com.example.riden;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("eH5J7FhF6w6yIwFkuRgLs6r3IAUIKktKOk6qOOeK")
                .clientKey("hAqK5klMLOr43tAbY3AWWh0gw8r3DAI0wF1F9xjL")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }

}
