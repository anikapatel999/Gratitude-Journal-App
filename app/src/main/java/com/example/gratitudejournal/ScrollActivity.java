package com.example.gratitudejournal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.parse.ParseUser;

public class ScrollActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll);

        //TODO:
        // 1. query posts by user, using the greater than / less than in the CalendarActivity
        //    method to make sure they're (for example)
        //    10 entries before and 10 entries after the desired date
        // 2. Also make sure that (!mood.equalTo("skip) || !text.equalTo("No entry"))
        //    so that we don't display any fully empty entries
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbarmenu, menu);
        return true;
    }

    public void onLogout(MenuItem item) {
        ParseUser.logOut();
        ParseUser currentUser = ParseUser.getCurrentUser(); // this will now be null
        Intent intent = new Intent(ScrollActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void onHome(MenuItem item) {
        Intent intent = new Intent(ScrollActivity.this, HomeActivity.class);
        startActivity(intent);
        // finish();
    }

    public void onSettings(MenuItem item) {
        Intent intent = new Intent(ScrollActivity.this, SettingsActivity.class);
        startActivity(intent);
        setVisible(false);
        //finish();
    }
}