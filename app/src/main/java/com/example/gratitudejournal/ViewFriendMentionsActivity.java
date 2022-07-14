package com.example.gratitudejournal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ViewFriendMentionsActivity extends AppCompatActivity {

    ListView lvFriends;

    public static final String TAG = "ViewMentionsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_friend_mentions);

        lvFriends = findViewById(R.id.lvFriends);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.new_color)));

        //ArrayList<String> friendUsernames = getIntent().getStringArrayExtra("friends");
        ArrayList<String> friendUsernames = getIntent().getStringArrayListExtra("friends");
        Log.i(TAG, "aaaaa" + String.valueOf(friendUsernames));

        ArrayList<String> userFreq = new ArrayList<>();

        Set<String> distinct = new HashSet<>(friendUsernames);
        for (String s: distinct) {
            System.out.println(s + ": " + Collections.frequency(friendUsernames, s));
            if (Collections.frequency(friendUsernames, s) > 1) {
                userFreq.add(s + " recently mentioned you in " + Collections.frequency(friendUsernames, s) + " entries");
                //Log.i(TAG, String.valueOf(userFreq));
            }
            else {
                userFreq.add(s + " recently mentioned you in an entry");
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(ViewFriendMentionsActivity.this, android.R.layout.simple_list_item_1, userFreq);
        lvFriends.setAdapter(adapter);

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ViewFriendMentionsActivity.this, ViewMentionsActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbarmenu, menu);
        menu.findItem(R.id.mentions).setVisible(false);
        return true;
    }

    public void onMentions(MenuItem item) {
        Intent intent = new Intent(ViewFriendMentionsActivity.this, ViewMentionsActivity.class);
        startActivity(intent);
    }

    public void onLogout(MenuItem item) {
        ParseUser.logOut();
        ParseUser currentUser = ParseUser.getCurrentUser(); // this will now be null
        Intent intent = new Intent(ViewFriendMentionsActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void onHome(MenuItem item) {
        Intent intent = new Intent(ViewFriendMentionsActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    public void onSettings(MenuItem item) {
        Intent intent = new Intent(ViewFriendMentionsActivity.this, SettingsActivity.class);
        startActivity(intent);
        finish();
        // setVisible(false);
    }
}