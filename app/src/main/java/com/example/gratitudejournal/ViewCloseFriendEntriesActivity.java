package com.example.gratitudejournal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;
import com.example.myapplication.Entry;

public class ViewCloseFriendEntriesActivity extends AppCompatActivity {

    public static final String TAG = "ViewCloseFriendEntriesActivity";

    List<Entry> allEntries = new ArrayList<>();
    private RecyclerView rvEntries;
    private EndlessRecyclerViewScrollListener scrollListener;
    protected EntriesAdapter adapter;


    ArrayList<String> closeFriendUsernames = new ArrayList<>(); //getIntent().getStringArrayListExtra("closeFriends");
    ArrayList<String> entryIds = new ArrayList<>(); //getIntent().getStringArrayListExtra("entryIds");
    String selectedUser; //getIntent().getStringExtra("username");
    ArrayList<String> entriesToDisplay = new ArrayList<>();

    ArrayList<String> closeFriendUsernames2 = new ArrayList<>();
    ArrayList<Mentions> mentionsToDelete = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_close_friend_entries);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        LinearLayout ll = findViewById(R.id.ll);
        ll.setBackgroundResource(R.color.warm);

        rvEntries = (RecyclerView) findViewById(R.id.rvEntries);
        adapter = new EntriesAdapter(this, allEntries);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvEntries.setLayoutManager(linearLayoutManager);

        // set the adapter on the recycler view
        rvEntries.setAdapter(adapter);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.new_color)));

        closeFriendUsernames = getIntent().getStringArrayListExtra("closeFriends");
        entryIds = getIntent().getStringArrayListExtra("entryIds");
        selectedUser = getIntent().getStringExtra("username");

        Log.i(TAG, "size " + String.valueOf(closeFriendUsernames.size()));
        for (int i = 0; i < closeFriendUsernames.size(); i++){
            Log.i(TAG, "size USERNAME" + closeFriendUsernames.get(i) + " " + i);
            if (!closeFriendUsernames.get(i).equals(selectedUser)){
                closeFriendUsernames2.add(closeFriendUsernames.get(i));
            }
        }
        //closeFriendUsernames.remove(selectedUser);
        Log.i(TAG, "size" + String.valueOf(closeFriendUsernames.size()));

        getMentions();


    }

    private void getMentions() {
        ParseUser currentUser = ParseUser.getCurrentUser();
        com.example.myapplication.User currentUser2 = (com.example.myapplication.User) currentUser;

//        for (int i = 0; i < entryIds.size(); i++) {
            ParseQuery<Mentions> query = new ParseQuery(Mentions.class);
            query.whereEqualTo("fromUser", selectedUser);
            //query.whereMatches("objectId", entryIds.get(i));
            // Log.i(TAG, "mentions query for: " + selectedUser + " " + entryIds.get(i));
            query.findInBackground(new FindCallback<Mentions>() {
                @Override
                public void done(List<Mentions> objects, ParseException e) {
                    // Log.i(TAG, "mentions query queried " + objects.size());
                    if (objects.size() > 0) {
                        for (int j = 0; j < objects.size(); j++) {
                            for (int i = 0; i < entryIds.size(); i++) {
                                if (objects.get(j).getObjectId().equals(entryIds.get(i))) {
                                    entriesToDisplay.add(objects.get(j).getMentionedEntry());
                                    mentionsToDelete.add(objects.get(j));
                                    if (i == entryIds.size() - 1 && j == objects.size() - 1) {
                                        getEntries();
                                    }
                                }
                                else {
                                    if (i == entryIds.size() - 1 && j == objects.size() - 1) {
                                        getEntries();
                                    }
                                }
                            }
                        }
                    }
                }
            });
    }

    private void getEntries() {
        // ParseUser currentUser = ParseUser.getCurrentUser();
        Log.i(TAG, "in getEntries " + entriesToDisplay.size());
        for (int i = 0; i < entriesToDisplay.size(); i++) {
            ParseQuery<Entry> query2 = ParseQuery.getQuery(Entry.class);
            query2.whereMatches("objectId", entriesToDisplay.get(i));
            int finalI = i;
            try {
                allEntries.addAll(query2.find());
                if (finalI == entriesToDisplay.size() - 1) {
                    adapter.notifyDataSetChanged();
                    Log.i(TAG, "allEntries size " + allEntries.size());
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        deleteMentions();
    }

    private void deleteMentions() {
        for (int i = 0; i < mentionsToDelete.size(); i++) {
            Mentions mention = mentionsToDelete.get(i);
            Log.i(TAG, "deleted");
            //TODO: UNCOMMENT THE DELETE HERE - just commented out for debugging / testing
//            try {
//                mention.delete();
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ViewCloseFriendEntriesActivity.this, ViewCloseFriendMentionsActivity.class);
        intent.putExtra("closeFriends", closeFriendUsernames2);
        intent.putExtra("entryIds", entryIds);
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
        Intent intent = new Intent(ViewCloseFriendEntriesActivity.this, ViewMentionsActivity.class);
        startActivity(intent);
    }

    public void onLogout(MenuItem item) {
        ParseUser.logOut();
        ParseUser currentUser = ParseUser.getCurrentUser(); // this will now be null
        Intent intent = new Intent(ViewCloseFriendEntriesActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void onHome(MenuItem item) {
        Intent intent = new Intent(ViewCloseFriendEntriesActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    public void onSettings(MenuItem item) {
        Intent intent = new Intent(ViewCloseFriendEntriesActivity.this, SettingsActivity.class);
        startActivity(intent);
        finish();
        // setVisible(false);
    }
}