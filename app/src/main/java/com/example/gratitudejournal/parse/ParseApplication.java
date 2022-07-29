package com.example.gratitudejournal.parse;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseUser;

public class ParseApplication extends Application {

//    String amazing = "Amazing";
//    String good = "Good";
//    String okay = "Okay";
//    String bad = "Bad";
//    String terrible = "Terrible";
//    String skip = "skip";

    @Override
    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(Entry.class);
        ParseObject.registerSubclass(Mentions.class);
        ParseUser.registerSubclass(User.class);

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("A49wxJrO5FIsjqnJRbwtLFKXD3VIcuHy0jinNiPo")
                .clientKey("yzsPoaqQut4PUVXZkBgk2n40LCfBlwDyMf5M6Bi9")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}