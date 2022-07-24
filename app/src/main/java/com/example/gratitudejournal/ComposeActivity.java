package com.example.gratitudejournal;

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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.jjoe64.graphview.series.DataPoint;
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
    float x1, x2, y1, y2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        etText = findViewById(R.id.etText);
        btnMood = findViewById(R.id.btnMood);
        btnSave = findViewById(R.id.btnSave);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.new_color)));

        ConstraintLayout cl = findViewById(R.id.cl);
        cl.setBackgroundResource(R.color.warm);

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
        if (mood.equals(Globals.skip)) {
            mood = "No mood selected";
        }

        // set the mood as the text for the button
        btnMood.setText("Edit mood: " + mood);

        // put the entry's text into the editText so the user can edit their entry
        // if one already exists
        if (!text.equals(Globals.no_entry)) {
            etText.setText(text);
        }

        // change the colors of the mood and save buttons depending on the mood the user
        // has selected

        switch (mood) {
            case Globals.terrible:
                btnMood.setBackgroundColor(btnMood.getContext().getResources().getColor(R.color.terrible));
                btnSave.setBackgroundColor(btnSave.getContext().getResources().getColor(R.color.terrible));
                break;

            case Globals.bad:
                btnMood.setBackgroundColor(btnMood.getContext().getResources().getColor(R.color.bad));
                btnSave.setBackgroundColor(btnSave.getContext().getResources().getColor(R.color.bad));
                break;

            case Globals.okay:
                btnMood.setBackgroundColor(btnMood.getContext().getResources().getColor(R.color.okay));
                btnSave.setBackgroundColor(btnSave.getContext().getResources().getColor(R.color.okay));
                break;

            case Globals.good:
                btnMood.setBackgroundColor(btnMood.getContext().getResources().getColor(R.color.good));
                btnSave.setBackgroundColor(btnSave.getContext().getResources().getColor(R.color.good));
                break;

            case Globals.amazing:
                btnMood.setBackgroundColor(btnMood.getContext().getResources().getColor(R.color.amazing));
                btnSave.setBackgroundColor(btnSave.getContext().getResources().getColor(R.color.amazing));
                break;

            default:
                btnMood.setBackgroundColor(btnMood.getContext().getResources().getColor(R.color.skip));
                btnSave.setBackgroundColor(btnSave.getContext().getResources().getColor(R.color.skip));
                break;
        }

        // navigate back to the mood selection activity if the user clicks on the mood button
        btnMood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Mood button");
//                Intent intent = new Intent(ComposeActivity.this, MoodActivity.class);
//                startActivity(intent);
                onBackPressed();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
//                finish();
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
                    currentText = Globals.no_entry;
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
                            Toast.makeText(ComposeActivity.this, "Error while saving!", Toast.LENGTH_SHORT).show();
                        }
                        Log.i(TAG, "Post save was successful " + finalCurrentText, e);
                        Toast.makeText(ComposeActivity.this, "Entry saved!", Toast.LENGTH_SHORT).show();
                    }
                });
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
                    //Intent i = new Intent(ComposeActivity.this, QuoteActivity.class);
                    Intent i = new Intent(ComposeActivity.this, MentionFriendsActivity.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    Log.i("swiped left", "it worked");
                    //finish();
                }
                break;
        }
        return false;
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbarmenu, menu);
        menu.findItem(R.id.mentions).setVisible(false);
        return true;
    }

    public void onMentions(MenuItem item) {
        Intent intent = new Intent(ComposeActivity.this, ViewMentionsActivity.class);
        startActivity(intent);
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
        // setVisible(false);
        //finish();
    }

    public void onSettings(MenuItem item) {
        Intent intent = new Intent(ComposeActivity.this, SettingsActivity.class);
        startActivity(intent);
        // setVisible(false);
        //finish();
    }

}