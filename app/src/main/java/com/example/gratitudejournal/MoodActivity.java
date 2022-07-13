package com.example.gratitudejournal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.example.myapplication.Entry;
import com.example.myapplication.User;
import com.parse.SaveCallback;

public class MoodActivity extends AppCompatActivity {

    // if mood != null: send an intent straight to compose (because they've already picked an emoji
    // otherwise do all the mood selection stuff

    // if navigating here from the onclick for the edit mood button
    // (which will be a button at the top of the compose activity),
    // set the mood to null before firing the intent

    float x1, x2, y1, y2;
    private Button btnAmazing;
    private Button btnGood;
    private Button btnOkay;
    private Button btnBad;
    private Button btnTerrible;
    public static final String TAG = "MoodActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood);
        btnAmazing = findViewById(R.id.btnAmazing);
        btnGood = findViewById(R.id.btnGood);
        btnOkay = findViewById(R.id.btnOkay);
        btnBad = findViewById(R.id.btnBad);
        btnTerrible = findViewById(R.id.btnTerrible);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.new_color)));

        btnAmazing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Amazing button");
                ParseUser currentUser = ParseUser.getCurrentUser();
                // User currentUser = (User) ParseUser.getCurrentUser();
                String mood = "Amazing";
                saveEntry(mood, currentUser);
            }
        });

        btnGood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Good button");
                ParseUser currentUser = ParseUser.getCurrentUser();
                // User currentUser = (User) ParseUser.getCurrentUser();
                String mood = "Good";
                saveEntry(mood, currentUser);
            }
        });

        btnOkay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Okay button");
                ParseUser currentUser = ParseUser.getCurrentUser();
                // User currentUser = (User) ParseUser.getCurrentUser();
                String mood = "Okay";
                saveEntry(mood, currentUser);
            }
        });

        btnBad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Bad button");
                ParseUser currentUser = ParseUser.getCurrentUser();
                // User currentUser = (User) ParseUser.getCurrentUser();
                String mood = "Bad";
                saveEntry(mood, currentUser);
            }
        });

        btnTerrible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Terrible button");
                ParseUser currentUser = ParseUser.getCurrentUser();
                // User currentUser = (User) ParseUser.getCurrentUser();
                String mood = "Terrible";
                saveEntry(mood, currentUser);
            }
        });
    }

    private void saveEntry(String mood, ParseUser currentUser) { // User currentUser || ParseUser currentUser
        Log.i(TAG, "got to saveEntry");
        User currentUser2 = (User) currentUser;
        if (currentUser2.getCurrentEntry() == null) {
            Log.i(TAG, "ENTRY got to if statement");
            Entry entry = new Entry();
            entry.setUser(currentUser2);
            entry.setMood(mood);
            entry.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    Log.i(TAG, "ENTRY got to save in background");
                    if (e != null) {
                        Log.e(TAG, "ENTRY Issue with saving", e);
                        Toast.makeText(MoodActivity.this, "Error while saving!", Toast.LENGTH_SHORT).show();
                    }
                    Log.i(TAG, "ENTRY Post save was successful", e);
                }
            });
            currentUser2.setCurrentEntry(entry);
            Log.i(TAG, "hello" + entry.getMood().toString());

        } else {
            // currentUser2.getCurrentEntry().getMood();
            currentUser2.getCurrentEntry().setMood(mood);
        }
        try {
            currentUser2.save();
            Log.e(TAG, "USER currentEntry saved");
        } catch (ParseException e) {
            e.printStackTrace();
            Log.e(TAG, "USER Issue with saving", e);
        }
        currentUser2.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                Log.i(TAG, "got to save in background");
                if (e != null) {
                    Log.e(TAG, "Issue with saving", e);
                    Toast.makeText(MoodActivity.this, "Error while saving!", Toast.LENGTH_SHORT).show();
                }
                Log.i(TAG, "Post save was successful", e);
            }
        });
    }

    public boolean onTouchEvent(MotionEvent touchEvent){
        switch(touchEvent.getAction()){
            case MotionEvent.ACTION_DOWN:
                x1 = touchEvent.getX();
                y1 = touchEvent.getY();
                break;
            case MotionEvent.ACTION_UP:
                x2 = touchEvent.getX();
                y2 = touchEvent.getY();

                if(x1 > x2) {
                    ParseUser currentUser = ParseUser.getCurrentUser();
                    User currentUser2 = (User) currentUser;
                    if(currentUser2.getCurrentEntry() == null) {
                        // Log.i(TAG, "IF STATEMENT EXECUTED" + currentUser.toString());
                        saveEntry("skip", currentUser);
                    }
                    Intent i = new Intent(MoodActivity.this, ComposeActivity.class);
//                    Intent i = new Intent(MoodActivity.this, QuoteActivity.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    Log.i("swiped left", "it worked");
                    // finish();
                }
            break;
        }
        return false;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbarmenu, menu);
        menu.findItem(R.id.mentions).setVisible(false);
        return true;
    }

    public void onMentions(MenuItem item) {
        Intent intent = new Intent(MoodActivity.this, ViewMentionsActivity.class);
        startActivity(intent);
    }

    public void onLogout(MenuItem item) {
        ParseUser.logOut();
        ParseUser currentUser = ParseUser.getCurrentUser(); // this will now be null
        Intent intent = new Intent(MoodActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void onHome(MenuItem item) {
        Intent intent = new Intent(MoodActivity.this, HomeActivity.class);
        startActivity(intent);
        // finish();
    }

    public void onSettings(MenuItem item) {
        Intent intent = new Intent(MoodActivity.this, SettingsActivity.class);
        startActivity(intent);
        // setVisible(false);
        //finish();
    }

}