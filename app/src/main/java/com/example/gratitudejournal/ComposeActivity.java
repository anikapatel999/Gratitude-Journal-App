package com.example.gratitudejournal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
        // TRYING TO LOAD THE MOOD STRING AND THE CORRECT MOOD COLOR INTO THE BUTTON
        User currentUser = (User) ParseUser.getCurrentUser();
        Entry entry = currentUser.getCurrentEntry();
        try {
            String text = entry.fetchIfNeeded().getString("text");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // entry.setMood("hello");
        // entry.saveInBackground();
        //Log.i(TAG, entry.getMood());
        String mood = null;
        try {
            mood = entry.fetchIfNeeded().getString("mood");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (mood.equals("skip")) {
            mood = "No mood selected";
        }
        btnMood.setText(mood);
        //btnMood.setBackgroundColor(3);
        btnSave = findViewById(R.id.btnSave);
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