package com.example.gratitudejournal.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.gratitudejournal.Globals;
import com.example.gratitudejournal.R;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.example.gratitudejournal.parse.Entry;
import com.example.gratitudejournal.parse.User;
import com.parse.SaveCallback;

public class MoodActivity extends AppCompatActivity {

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

        ConstraintLayout cl = findViewById(R.id.cl);
        cl.setBackgroundResource(R.color.warm);

        btnAmazing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Amazing button");
                ParseUser currentUser = ParseUser.getCurrentUser();
                String mood = Globals.amazing;
                saveEntry(mood, currentUser);
            }
        });

        btnGood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Good button");
                ParseUser currentUser = ParseUser.getCurrentUser();
                String mood = Globals.good;
                saveEntry(mood, currentUser);
            }
        });

        btnOkay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Okay button");
                ParseUser currentUser = ParseUser.getCurrentUser();
                String mood = Globals.okay;
                saveEntry(mood, currentUser);
            }
        });

        btnBad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Bad button");
                ParseUser currentUser = ParseUser.getCurrentUser();
                String mood = Globals.bad;
                saveEntry(mood, currentUser);
            }
        });

        btnTerrible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Terrible button");
                ParseUser currentUser = ParseUser.getCurrentUser();
                String mood = Globals.terrible;
                saveEntry(mood, currentUser);
            }
        });
    }

    private void saveEntry(String mood, ParseUser currentUser) {
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
                        saveEntry(Globals.skip, currentUser);
                    }
                    Intent i = new Intent(MoodActivity.this, ComposeActivity.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    Log.i("swiped left", "it worked");
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
    }

    public void onSettings(MenuItem item) {
        Intent intent = new Intent(MoodActivity.this, SettingsActivity.class);
        startActivity(intent);
    }

}