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

import org.json.JSONArray;

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
        User currentUser2 = (User) currentUser;
        if (currentUser2.getCurrentEntry() != null) {
            //Log.i(TAG, currentUser2.getCurrentEntry().getText());
                Entry lastEntry = currentUser2.getCurrentEntry();
                String mood = null; //getMood();
                try {
                    mood = currentUser2.getCurrentEntry().fetchIfNeeded().getString("mood");
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                currentUser.remove("currentEntry");

                currentUser.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        Log.i(TAG, "CURRENTUSER got to save in background");
                        if (e != null) {
                            Log.e(TAG, "CURRENTUSER Issue with saving", e);
                        }
                        Log.i(TAG, "CURRENTUSER Post save was successful", e);
                    }
                });
                JSONArray moods = currentUser2.getMoods();
                moods.put(mood);
                currentUser2.setMoods(moods);
                currentUser2.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        Log.i(TAG, "CURRENTUSER2 got to save in background");
                        if (e != null) {
                            Log.e(TAG, "CURRENTUSER2 Issue with saving", e);
                        }
                        Log.i(TAG, "CURRENTUSER2 Post save was successful", e);
                    }
                });

                String text = null; //getMood();

                try {
                    text = lastEntry.fetchIfNeeded().getString("text");
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if(mood.equals("skip") && text.equals("No entry")){
                    lastEntry.deleteInBackground();
                }

                Log.i(TAG, "the alarm worked");
        }
    }

}
