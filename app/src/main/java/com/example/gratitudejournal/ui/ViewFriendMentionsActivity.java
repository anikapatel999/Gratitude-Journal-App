package com.example.gratitudejournal.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.gratitudejournal.R;
import com.example.gratitudejournal.parse.Mentions;
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

    public static final String TAG = "ViewFriendMentionsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_friend_mentions);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        lvFriends = findViewById(R.id.lvFriends);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.new_color)));

        ConstraintLayout cl = findViewById(R.id.cl);
        cl.setBackgroundResource(R.color.warm);

        ArrayList<String> friendUsernames = getIntent().getStringArrayListExtra("friends");
        ArrayList<String> entryIds = getIntent().getStringArrayListExtra("entryIds");
        ArrayList<String> userFreq = new ArrayList<>();

        Set<String> distinct = new HashSet<>(friendUsernames);
        for (String s: distinct) {
            System.out.println(s + ": " + Collections.frequency(friendUsernames, s));
            if (Collections.frequency(friendUsernames, s) > 1) {
                userFreq.add(s + " recently mentioned you in " + Collections.frequency(friendUsernames, s) + " entries");
            }
            else {
                userFreq.add(s + " recently mentioned you in an entry");
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(ViewFriendMentionsActivity.this, android.R.layout.simple_list_item_1, userFreq);
        lvFriends.setAdapter(adapter);

        for (int i = 0; i < entryIds.size(); i++) {
            ParseUser currentUser = ParseUser.getCurrentUser();
            ParseQuery<Mentions> query = new ParseQuery(Mentions.class);
            query.whereEqualTo("objectId", entryIds.get(i));
            List<Mentions> mentions = null;
            try {
                mentions = query.find();
                for (int j = 0; j < mentions.size(); j++) {
                    Mentions m = mentions.get(j);
                    // TODO: UNCOMMENT THE DELETE LINE BELOW!!
                    m.delete();
                    Log.i(TAG, "deleted " + entryIds.get(i));
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
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
    }
}