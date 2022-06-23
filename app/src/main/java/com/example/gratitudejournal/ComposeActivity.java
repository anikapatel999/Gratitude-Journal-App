package com.example.gratitudejournal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.example.myapplication.User;
import com.example.myapplication.Entry;
import com.parse.SaveCallback;

import java.io.File;

public class ComposeActivity extends AppCompatActivity {

    public static final String TAG = "ComposeActivity";
    private EditText etText;
    private Button btnMood;
    private Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        etText = findViewById(R.id.etText);
        btnMood = findViewById(R.id.btnMood);
        btnSave = findViewById(R.id.btnSave);

        // get the user's current journal entry
        User currentUser = (User) ParseUser.getCurrentUser();
        Entry entry = currentUser.getCurrentEntry();
        String text = null;
        try {
            text = entry.fetchIfNeeded().getString("text");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // get the user's current mood selection
        String mood = null;
        try {
            mood = entry.fetchIfNeeded().getString("mood");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (mood.equals("skip")) {
            mood = "No mood selected";
        }

        // set the mood as the text for the button
        btnMood.setText("Edit mood: " + mood);

        // put the entry's text into the editText so the user can edit their entry
        // if one already exists
        if (!text.equals("No entry")) {
            etText.setText(text);
        }

        // change the colors of the mood and save buttons depending on the mood the user
        // has selected
        if(mood.equals("Amazing")){
            btnMood.setBackgroundColor(0xFFF8CC7F);
            btnSave.setBackgroundColor(0xFFF8CC7F);
        }

        else if(mood.equals("Good")){
            btnMood.setBackgroundColor(0xFFD2B08C);
            btnSave.setBackgroundColor(0xFFD2B08C);
        }

        else if(mood.equals("Okay")){
            btnMood.setBackgroundColor(0xFF808080);
            btnSave.setBackgroundColor(0xFF808080);
        }

        else if(mood.equals("Bad")){
            btnMood.setBackgroundColor(0xFF374C56);
            btnSave.setBackgroundColor(0xFF374C56);
        }

        else if(mood.equals("Terrible")){
            btnMood.setBackgroundColor(0xFF04202F);
            btnSave.setBackgroundColor(0xFF04202F);
        }

        // navigate back to the mood selection activity if the user clicks on the mood button
        btnMood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Mood button");
                Intent intent = new Intent(ComposeActivity.this, MoodActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        String finalMood = mood;
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Save button");
                String currentText = etText.getText().toString();
                if (currentText.equals("")) {
                    // Log.i(TAG, "this shouldn't be true");
                    currentText = "No entry";
                }
                entry.setText(currentText);
                entry.setMood(finalMood);
                entry.setUser(currentUser);
                String finalCurrentText = currentText;
                entry.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        Log.i(TAG, "got to save in background");
                        if (e != null) {
                            Log.e(TAG, "Issue with saving", e);
                            // Toast.makeText(ComposeActivity.this, "Error while saving!", Toast.LENGTH_SHORT).show();
                        }
                        Log.i(TAG, "Post save was successful " + finalCurrentText, e);
                    }
                });
            }
        });
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbarmenu, menu);
        return true;
    }

    public void onLogout(MenuItem item) {
        ParseUser.logOut();
        ParseUser currentUser = ParseUser.getCurrentUser(); // this will now be null
        Intent intent = new Intent(ComposeActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void onHome(MenuItem item) {
        Intent intent = new Intent(ComposeActivity.this, HomeActivity.class);
        startActivity(intent);
        setVisible(false);
        //finish();
    }
}