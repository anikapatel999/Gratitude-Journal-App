package com.example.gratitudejournal;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.gratitudejournal.parse.Mentions;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.example.gratitudejournal.parse.User;
import com.parse.SaveCallback;
import com.example.gratitudejournal.parse.Entry;

import org.json.JSONArray;
import org.json.JSONException;

public class Alarm extends BroadcastReceiver {

    public static final String TAG = "Alarm";

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.i(TAG, "got to the alarm");
        ParseUser currentUser = ParseUser.getCurrentUser();
        User currentUser2 = (User) currentUser;
        if (currentUser2.getCurrentEntry() != null) {
            Entry lastEntry = currentUser2.getCurrentEntry();
            boolean sendFriendNotifs = currentUser2.getMentionFriends();
            boolean sendCloseFriendNotifs = currentUser2.getMentionCloseFriends();

            JSONArray fm = null;
            try {
                fm = lastEntry.fetchIfNeeded().getJSONArray("friendMentions");
            } catch (ParseException e) {
                e.printStackTrace();
            }

            JSONArray cfm = null;
            try {
                cfm = lastEntry.fetchIfNeeded().getJSONArray("closeFriendMentions");
            } catch (ParseException e) {
                e.printStackTrace();
            }

            String entryId = lastEntry.getObjectId();

            Log.i(TAG, String.valueOf(fm));
            Log.i(TAG, String.valueOf(cfm));
            Log.i(TAG, String.valueOf(entryId));

            if(fm.length() > 0 && sendFriendNotifs) {
                for (int i = 0; i < fm.length(); i++){
                    Mentions mention = new Mentions();
                    mention.setFromUser(currentUser.getUsername());
                    try {
                        mention.setToUser(fm.get(i).toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    mention.setMentionedEntry(entryId);
                    mention.setCloseFriend(false);
                    try {
                        mention.save();
                        Log.i(TAG, "friend mention saved");
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }

            if (cfm.length() > 0 && sendCloseFriendNotifs) {
                for (int i = 0; i < cfm.length(); i++){
                    Mentions mention = new Mentions();
                    mention.setFromUser(currentUser.getUsername());
                    try {
                        mention.setToUser(cfm.get(i).toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    mention.setMentionedEntry(entryId);
                    mention.setCloseFriend(true);
                    try {
                        mention.save();
                        Log.i(TAG, "close friend mention saved");
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }

                String mood = null;
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

                String text = null;

                try {
                    text = lastEntry.fetchIfNeeded().getString("text");
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if(mood.equals(Globals.skip) && text.equals(Globals.no_entry) && fm.length() == 0 && cfm.length() == 0){
                    lastEntry.deleteInBackground();
                }

                if(mood.equals("No mood selected") && text.equals(Globals.no_entry) && fm.length() == 0 && cfm.length() == 0){
                    lastEntry.deleteInBackground();
                }

                Log.i(TAG, "the alarm worked");
        }
    }

}
