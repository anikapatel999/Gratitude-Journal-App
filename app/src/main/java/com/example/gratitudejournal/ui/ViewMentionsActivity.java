package com.example.gratitudejournal.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.gratitudejournal.R;
import com.example.gratitudejournal.parse.Mentions;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import com.example.gratitudejournal.parse.User;

public class ViewMentionsActivity extends AppCompatActivity {
    public static final String TAG = "ViewMentionsActivity";
    TextView tvNoNew;
    Button btnFriends;
    Button btnCloseFriends;

    JSONArray friends = new JSONArray();
    JSONArray closeFriends = new JSONArray();

    ArrayList<String> friendUsernames = new ArrayList<>();
    ArrayList<String> closeFriendUsernames = new ArrayList<>();

    ArrayList<String> finalFriendUsernames = new ArrayList<>();
    ArrayList<String> finalCloseFriendUsernames = new ArrayList<>();

    ArrayList<String> finalFriendEntries = new ArrayList<>();
    ArrayList<String> finalCloseFriendEntries = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_mentions);

        tvNoNew = findViewById(R.id.tvNoNew);
        btnFriends = findViewById(R.id.btnFriends);
        btnCloseFriends = findViewById(R.id.btnCloseFriends);
        boolean set = false;

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.new_color)));

        ConstraintLayout cl = findViewById(R.id.cl);
        cl.setBackgroundResource(R.color.warm);

        ParseUser currentUser = ParseUser.getCurrentUser();
        User currentUser2 = (User) currentUser;

        friends = currentUser2.getFriends();
        closeFriends = currentUser2.getCloseFriends();
        getFriendUsernames();
        //getCloseFriendUsernames();

        btnFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewMentionsActivity.this, ViewFriendMentionsActivity.class);
                //finalFriendUsernames.toArray();
                intent.putExtra("friends", finalFriendUsernames);
                intent.putExtra("entryIds", finalFriendEntries);
                startActivity(intent);
                finish();
            }
        });

        btnCloseFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewMentionsActivity.this, ViewCloseFriendMentionsActivity.class);
                //finalCloseFriendUsernames.toArray();
                intent.putExtra("closeFriends", finalCloseFriendUsernames);
                intent.putExtra("entryIds", finalCloseFriendEntries);
                startActivity(intent);
                finish();
            }
        });
    }

    private boolean inArray(ArrayList arr, String fromUser) {
        boolean contained = false;

        for (int i = 0; i < arr.size(); i++) {
            Log.i(TAG, "ele " + i + ": " + arr.get(i).toString());
        }


        for (int i = 0; i < arr.size(); i++) {
            Log.i(TAG, "friends...: " + arr.get(i) + " " + fromUser);
            if (arr.get(i).toString().equals(fromUser)){
                contained = true;
                Log.i(TAG, "friend returned: " + arr.get(i) + " " + fromUser);
                break;
            }
        }
        return contained;
    }

    private void getCloseFriendUsernames() {
        //if there are no close friends, get the mentions
        // (makes sure this function is still called if the code below does not execute)
        if (closeFriends.length() == 0) {
            getMentions();
        }
        // for each index in the list of close friend pointers
        for (int i = 0; i < closeFriends.length(); i++) {
            Log.i(TAG, "A: " + closeFriendUsernames);
            try {
                // if the close friend at that index is of the User class, get their username
                // and add it to closeFriendUsernames
                if (closeFriends.get(i).getClass().equals(User.class)) {
                    Log.i(TAG, "B: " + closeFriendUsernames);
                    User a = (User) closeFriends.get(i);
                    String currentUsername = a.getUsername();
                    closeFriendUsernames.add(currentUsername);
                    // if we have gone through all the elements in closeFriends,
                    // call getMentions()
                    if (closeFriendUsernames.size() == closeFriends.length()) {
                        Log.i(TAG, "CLOSE FRIEND USERNAMES LIST" + closeFriendUsernames);
                        getMentions();
                    }
                    // if the close friend is not a User, get their username by querying for the
                    // matching objectId from parse and get the username from the object returned.
                    // Add this username to closeFriendUsernames
                } else {
                    JSONObject a = (JSONObject) closeFriends.get(i);
                    String temp = a.getString("objectId");
                    Log.i(TAG, "C: " + temp);
                    ParseQuery<User> query2 = new ParseQuery(User.class);
                    query2.whereEqualTo("objectId", temp);
                    int finalI = i;
                    query2.findInBackground(new FindCallback<User>() {
                        @Override
                        public void done(List<User> objects, ParseException e) {
                            Log.i(TAG, "D: " + objects);
                            if (objects.size() == 1) {
                                User closeFriend = objects.get(0);
                                String currentUsername2 = closeFriend.getUsername();
                                closeFriendUsernames.add(currentUsername2);
                                Log.i(TAG, "AA" + closeFriendUsernames + " " + finalI + " " + (closeFriends.length() - 1));
                                // if we have gone through all the elements in close friends,
                                // call getMentions()
                                if (closeFriendUsernames.size() == closeFriends.length()) {
                                    Log.i(TAG, "CLOSE FRIEND USERNAMES LIST" + closeFriendUsernames);
                                    getMentions();
                                }
                            }
                        }
                    });
                }

            } catch (JSONException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void getMentions() {

        ParseUser currentUser = ParseUser.getCurrentUser();
        User currentUser2 = (User) currentUser;

        ParseQuery<Mentions> query = new ParseQuery(Mentions.class);
        query.whereEqualTo("toUser", currentUser.getUsername());
        query.findInBackground(new FindCallback<Mentions>() {
            @Override
            public void done(List<Mentions> objects, ParseException e) {
                Log.i(TAG, "did the query");
                boolean friendsVis = false;
                boolean closeFriendsVis = false;
                if (objects.size() > 0) {
                    for(int i = 0; i < objects.size(); i++) {
                        boolean closeFriend = (boolean) objects.get(i).get("closeFriend");
                        Log.i(TAG, "lkdfvmdlkvm: " + closeFriend);

                        // if the user that sent the mention has the current user listed as a close friend:
                        if (closeFriend){
                            String usercf = objects.get(i).get("fromUser").toString();
                            String cfid = objects.get(i).getObjectId();
                            boolean cf = inArray(closeFriendUsernames, usercf);
                            // if the current user has the sender as a close friend
                            if (cf) {
                                //btnCloseFriends.setVisibility(View.VISIBLE);
                                closeFriendsVis = true;
                                Log.i(TAG, "close friends visible = true");
                                //tvNoNew.setVisibility(TextView.GONE);
                                finalCloseFriendUsernames.add(usercf);
                                finalCloseFriendEntries.add(cfid);
                            }
                            else {
                                String userf = objects.get(i).get("fromUser").toString();
                                String fid = objects.get(i).getObjectId();
                                boolean f = inArray(friendUsernames, userf);
                                // if the current user has the sender as a friend
                                if (f) {
                                    // btnFriends.setVisibility(View.VISIBLE);
                                    friendsVis = true;
                                    Log.i(TAG, "friends visible1 = true");
                                    //tvNoNew.setVisibility(TextView.GONE);
                                    finalFriendUsernames.add(userf);
                                    finalFriendEntries.add(fid);
                                }
                            }
                            // if the current user does not have the sender as a friend/close friend, do nothing
                        }
                        else {
                            //Log.i(TAG, "wheee: " + closeFriend);
                            String usercf = objects.get(i).get("fromUser").toString();
                            String cfid = objects.get(i).getObjectId();
                            boolean cf = inArray(closeFriendUsernames, usercf);
                            if (cf) {
                                //btnFriends.setVisibility(View.VISIBLE);
                                friendsVis = true;
                                Log.i(TAG, "friends visible2 = true");
                                //tvNoNew.setVisibility(TextView.GONE);
                                finalFriendUsernames.add(usercf);
                                finalFriendEntries.add(cfid);
                            }
                            else {
                                String userf = objects.get(i).get("fromUser").toString();
                                String fid = objects.get(i).getObjectId();
                                boolean f = inArray(friendUsernames, userf);
                                if (f) {
                                    //btnFriends.setVisibility(View.VISIBLE);
                                    friendsVis = true;
                                    Log.i(TAG, "friends visible3 = true");
                                    //tvNoNew.setVisibility(TextView.GONE);
                                    finalFriendUsernames.add(userf);
                                    finalFriendEntries.add(fid);
                                }
                            }
                        }
                    }
                }
                boolean friendNotifs = currentUser2.getFriendMentions();
                boolean closeFriendNotifs = currentUser2.getCloseFriendMentions();
                if (friendsVis && friendNotifs) {
                    btnFriends.setVisibility(View.VISIBLE);
                    Log.i(TAG, "set visible friends");
                }
                if (closeFriendsVis && closeFriendNotifs) {
                    btnCloseFriends.setVisibility(View.VISIBLE);
                    Log.i(TAG, "set visible close friends");
                }
                if (!friendNotifs && !closeFriendNotifs){
                    Log.i(TAG, "here");
                    String text = "You have mention notifications disabled!";
                    tvNoNew.setVisibility(View.VISIBLE);
                    tvNoNew.setText(text);
                }
                if (!friendsVis && !closeFriendsVis){
                    tvNoNew.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void getFriendUsernames() {
        Log.i(TAG, "called");

        //if there are no friends, get the close friends usernames
        // (makes sure this function is still called if the code below does not execute)
        if (friends.length() == 0) {
            getCloseFriendUsernames();
        }
        // for each index in the list of friend pointers
        for (int i = 0; i < friends.length(); i++) {
            Log.i(TAG, "Aaay: " + friendUsernames);
            try {
                // if the friend at that index is of the User class, get their username and add it
                // to friendUsernames
                if (friends.get(i).getClass().equals(User.class)) {
                    Log.i(TAG, "Bee: " + friendUsernames);
                    User a = (User) friends.get(i);
                    String currentUsername = a.getUsername();
                    friendUsernames.add(currentUsername);
                    // if we have gone through all the elements in friends,
                    // call getCloseFriendUsernames()
                    if (friendUsernames.size() == friends.length()) {
                        Log.i(TAG, "USER FRIEND USERNAMES LIST" + friendUsernames);
                        getCloseFriendUsernames();
                    }
                    // if the friend is not a User, get their username by querying for the
                    // matching objectId from parse and get the username from the object returned.
                    // Add this username to friendUsernames
                } else {
                    JSONObject a = (JSONObject) friends.get(i);
                    String temp = a.getString("objectId");
                    Log.i(TAG, "Cee: " + temp);
                    ParseQuery<User> query = new ParseQuery(User.class);
                    query.whereEqualTo("objectId", temp);
                    int finalI = i;
                    query.findInBackground(new FindCallback<User>() {
                        @Override
                        public void done(List<User> objects, ParseException e) {
                            Log.i(TAG, "Dee: " + objects);
                            if (objects.size() == 1) {
                                User friend = objects.get(0);
                                String currentUsername2 = friend.getUsername();
                                friendUsernames.add(currentUsername2);
                                Log.i(TAG, "AA" + friendUsernames + " " + finalI + " " + (friends.length() - 1));
                                // if we have gone through all the elements in friends,
                                // call getCloseFriendUsernames()
                                if (friendUsernames.size() == friends.length()) {
                                    Log.i(TAG, "FRIEND USERNAMES LIST" + friendUsernames);
                                    getCloseFriendUsernames();
                                }
                            }
                        }
                    });
                }

            } catch (JSONException ex) {
                ex.printStackTrace();
            }
        }
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ViewMentionsActivity.this, HomeActivity.class);
        finish();
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
        //finish();
    }

    public void onHome(MenuItem item) {
        Intent intent = new Intent(ViewMentionsActivity.this, HomeActivity.class);
        startActivity(intent);
        //finish();
    }

    public void onSettings(MenuItem item) {
        Intent intent = new Intent(ViewMentionsActivity.this, SettingsActivity.class);
        startActivity(intent);
        finish();
        // setVisible(false);
    }
}