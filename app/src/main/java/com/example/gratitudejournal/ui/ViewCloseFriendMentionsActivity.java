package com.example.gratitudejournal.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gratitudejournal.R;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ViewCloseFriendMentionsActivity extends AppCompatActivity {

    ListView lvCloseFriends;
    TextView tvSeenAll;

    public static final String TAG = "ViewCloseFriendMentionsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_close_friend_mentions);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        lvCloseFriends = findViewById(R.id.lvCloseFriends);
        tvSeenAll = findViewById(R.id.tvSeenAll);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.new_color)));

        ConstraintLayout cl = findViewById(R.id.cl);
        cl.setBackgroundResource(R.color.warm);

        ArrayList<String> closeFriendUsernames = getIntent().getStringArrayListExtra("closeFriends");
        ArrayList<String> entryIds = getIntent().getStringArrayListExtra("entryIds");
        ArrayList<String> userFreq = new ArrayList<>();

        if(closeFriendUsernames.size() == 0) {
            tvSeenAll.setVisibility(View.VISIBLE);
        }

        Set<String> distinct = new HashSet<>(closeFriendUsernames);
        for (String s: distinct) {
            System.out.println(s + ": " + Collections.frequency(closeFriendUsernames, s));
            if (Collections.frequency(closeFriendUsernames, s) > 1) {
                userFreq.add("click to view " + Collections.frequency(closeFriendUsernames, s) + " recent mentions from " + s);
            }
            else {
                userFreq.add("click to view a recent mention from " + s);
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(ViewCloseFriendMentionsActivity.this, android.R.layout.simple_list_item_1, userFreq);
        lvCloseFriends.setAdapter(adapter);

        lvCloseFriends.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String[] words = userFreq.get(position).split(" ");
                Toast.makeText(ViewCloseFriendMentionsActivity.this, "selected " + words[words.length - 1] + "!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ViewCloseFriendMentionsActivity.this, ViewCloseFriendEntriesActivity.class);
                intent.putExtra("username", words[words.length - 1]);
                intent.putExtra("closeFriends", closeFriendUsernames);
                intent.putExtra("entryIds", entryIds);
                startActivity(intent);
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ViewCloseFriendMentionsActivity.this, ViewMentionsActivity.class);
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
        Intent intent = new Intent(ViewCloseFriendMentionsActivity.this, ViewMentionsActivity.class);
        startActivity(intent);
    }

    public void onLogout(MenuItem item) {
        ParseUser.logOut();
        ParseUser currentUser = ParseUser.getCurrentUser(); // this will now be null
        Intent intent = new Intent(ViewCloseFriendMentionsActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void onHome(MenuItem item) {
        Intent intent = new Intent(ViewCloseFriendMentionsActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    public void onSettings(MenuItem item) {
        Intent intent = new Intent(ViewCloseFriendMentionsActivity.this, SettingsActivity.class);
        startActivity(intent);
        finish();
    }
}