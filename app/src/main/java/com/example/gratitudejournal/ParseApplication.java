package com.example.gratitudejournal;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // ParseObject.registerSubclass(Entry.class); //GOTTA MAKE AN ENTRY CLASS

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("A49wxJrO5FIsjqnJRbwtLFKXD3VIcuHy0jinNiPo")
                .clientKey("yzsPoaqQut4PUVXZkBgk2n40LCfBlwDyMf5M6Bi9")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}