package com.example.gratitudejournal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class ViewMentionsActivity extends AppCompatActivity {
    TextView tvNoNew;
    Button btnFriends;
    Button btnCloseFriends;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_mentions);

        tvNoNew = findViewById(R.id.tvNoNew);
        btnFriends = findViewById(R.id.btnFriends);
        btnCloseFriends = findViewById(R.id.btnCloseFriends);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.new_color)));

        ParseUser currentUser = ParseUser.getCurrentUser();
        ParseQuery<Mentions> query = new ParseQuery(Mentions.class);
        query.whereEqualTo("toUser", currentUser.getUsername());
        query.findInBackground(new FindCallback<Mentions>() {
            @Override
            public void done(List<Mentions> objects, ParseException e) {
                if (objects.size() > 0) {
                    tvNoNew.setVisibility(TextView.GONE);
                    btnFriends.setVisibility(View.VISIBLE);
                    btnCloseFriends.setVisibility(View.VISIBLE);
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbarmenu, menu);
        menu.findItem(R.id.mentions).setVisible(false);
        return true;
    }

    public void onMentions(MenuItem item) {
        Intent intent = new Intent(ViewMentionsActivity.this, ViewMentionsActivity.class);
        startActivity(intent);
    }

    public void onLogout(MenuItem item) {
        ParseUser.logOut();
        ParseUser currentUser = ParseUser.getCurrentUser(); // this will now be null
        Intent intent = new Intent(ViewMentionsActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void onHome(MenuItem item) {
        Intent intent = new Intent(ViewMentionsActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    public void onSettings(MenuItem item) {
        Intent intent = new Intent(ViewMentionsActivity.this, SettingsActivity.class);
        startActivity(intent);
        finish();
        // setVisible(false);
    }
}