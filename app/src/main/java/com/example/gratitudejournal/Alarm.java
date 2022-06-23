package com.example.gratitudejournal;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.example.myapplication.User;
import com.parse.SaveCallback;
import com.example.myapplication.Entry;

public class Alarm extends BroadcastReceiver {

    public static final String TAG = "Alarm";

    @Override
    public void onReceive(Context context, Intent intent) {
        // MediaPlayer mediaPlayer = MediaPlayer.create(context, Settings.System.DEFAULT_RINGTONE_URI);
        // mediaPlayer.start();

        /*
        // MUCH NICER BUT PARSE GETS MAD
        User currentUser = (User) ParseUser.getCurrentUser();
        currentUser.setCurrentEntry(null); //IT DOESNT LIKE ME SETTING IT TO NULL ACK
         */
        //User currentUser = (User) ParseUser.getCurrentUser();
        //currentUser.unset("currentEntry"); //IT DOESNT LIKE ME SETTING IT TO NULL ACK
        Log.i(TAG, "got to the alarm");
        ParseUser currentUser = ParseUser.getCurrentUser();
        currentUser.remove("currentEntry");

//        ParseUser currentUser = ParseUser.getCurrentUser();
//        User currentUser2 = (User) currentUser;
//        Entry entry = new Entry();
//        entry.setUser(currentUser);
//        entry.setMood("temporary entry");
//        entry.saveInBackground();
//        currentUser2.setCurrentEntry(entry); //IT DOESNT LIKE ME SETTING IT TO NULL ACK
        // if i want to be sneaky, i could create one post and have the pointer point to it, then in the
        // if statement where it checks if the pointer == null, i do if pointer == null || pointer == that one post
        // but is there a better option? i feel like there should be @_@
        currentUser.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                Log.i(TAG, "got to save in background");
                if (e != null) {
                    Log.e(TAG, "Issue with saving", e);
                }
                Log.i(TAG, "Post save was successful", e);
            }
        });
        Log.i(TAG, "the alarm worked");
    }
}
