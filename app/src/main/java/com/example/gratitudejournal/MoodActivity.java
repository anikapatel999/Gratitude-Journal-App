package com.example.gratitudejournal;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MoodActivity extends AppCompatActivity {

    // if mood != null: send an intent straight to compose (because they've already picked an emoji
    // otherwise do all the mood selection stuff

    // if navigating here from the onclick for the edit mood button
    // (which will be a button at the top of the compose activity),
    // set the mood to null before firing the intent

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood);
    }

}