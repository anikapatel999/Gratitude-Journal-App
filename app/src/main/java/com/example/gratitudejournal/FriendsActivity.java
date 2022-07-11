package com.example.gratitudejournal;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.LayoutTransition;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.example.myapplication.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FriendsActivity extends AppCompatActivity {

    public static final String TAG = "FriendsActivity";

    EditText etAddFriend;
    EditText etAddCloseFriend;

    Button btnAddFriend;
    Button btnAddCloseFriend;

    Spinner sRemoveFriend;
    Spinner sRemoveCloseFriend;

    LinearLayout llFriends;
    TextView tvFriendsCardviewText;
    TextView tvFriendsList;

    LinearLayout llCloseFriends;
    TextView tvCloseFriendsCardviewText;
    TextView tvCloseFriendsList;

    JSONArray friends = new JSONArray();
    JSONArray closeFriends = new JSONArray();

    ArrayList<String> friendUsernames = new ArrayList<>();
    ArrayList<String> closeFriendUsernames = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        etAddFriend = findViewById(R.id.etAddFriend);
        etAddCloseFriend = findViewById(R.id.etAddCloseFriend);

        btnAddFriend = findViewById(R.id.btnAddFriend);
        btnAddCloseFriend = findViewById(R.id.btnAddCloseFriend);

        sRemoveFriend = findViewById(R.id.sRemoveFriend);
        sRemoveCloseFriend = findViewById(R.id.sRemoveCloseFriend);

        llFriends = findViewById(R.id.llFriends);
        tvFriendsCardviewText = findViewById(R.id.tvFriendsCardviewText);
        tvFriendsList = findViewById(R.id.tvFriendsList);
        llFriends.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);

        llCloseFriends = findViewById(R.id.llCloseFriends);
        tvCloseFriendsCardviewText = findViewById(R.id.tvCloseFriendsCardviewText);
        tvCloseFriendsList = findViewById(R.id.tvCloseFriendsList);
        llCloseFriends.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.new_color)));

        ParseUser currentUser = ParseUser.getCurrentUser();
        User currentUser2 = (User) currentUser;
        friends = currentUser2.getFriends();
        closeFriends = currentUser2.getCloseFriends();

        // Make list of friend usernames
        getFriendUsernames();


        // Make list of close friend usernames
        getCloseFriendUsernames();


        //FOR ADDING FRIENDS
        btnAddFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etAddFriend.getText().toString();
                if (currentUser.getUsername().equals(username)) {
                    Toast.makeText(FriendsActivity.this, "You cannot add yourself!", Toast.LENGTH_SHORT).show();
                } else {
                    ParseQuery<User> query = new ParseQuery(User.class);
                    query.whereEqualTo("username", username);
                    query.findInBackground(new FindCallback<User>() {
                        @Override
                        public void done(List<User> objects, ParseException e) {
                            boolean found = false;
                            if (objects.size() == 1) {
                                User newFriend = objects.get(0);
                                String nfID = newFriend.getObjectId();
                                for (int i = 0; i < friends.length(); i++) {
                                    try {
                                        if (friends.get(i).getClass().equals(User.class)) {
                                            Log.i(TAG, "class issue");
                                            User a = (User) friends.get(i);
                                            String uID = a.getObjectId();
                                            if (nfID.equals(uID)) {
                                                found = true;
                                                Toast.makeText(FriendsActivity.this, "This user is already in your friends list!", Toast.LENGTH_SHORT).show();
                                            }
                                        } else {
                                            JSONObject a = (JSONObject) friends.get(i);
                                            String id = a.getString("objectId");
                                            Log.i(TAG, "this HeLLO " + friends);
                                            if (nfID.equals(id)) {
                                                Toast.makeText(FriendsActivity.this, "This user is already in your friends list!", Toast.LENGTH_SHORT).show();
                                                found = true;
                                                break;
                                            }
                                        }

                                    } catch (JSONException ex) {
                                        ex.printStackTrace();
                                    }
                                }
                                for (int i = 0; i < closeFriends.length(); i++) {
                                    try {
                                        if (closeFriends.get(i).getClass().equals(User.class)) {
                                            Log.i(TAG, "class issue");
                                            User a = (User) closeFriends.get(i);
                                            String uID = a.getObjectId();
                                            if (nfID.equals(uID)) {
                                                found = true;
                                                Toast.makeText(FriendsActivity.this, "This user is already in your close friends list!", Toast.LENGTH_SHORT).show();
                                            }
                                        } else {
                                            JSONObject a = (JSONObject) closeFriends.get(i);
                                            String id = a.getString("objectId");
                                            Log.i(TAG, "this HeLLO " + closeFriends);
                                            if (nfID.equals(id)) {
                                                Toast.makeText(FriendsActivity.this, "This user is already in your close friends list!", Toast.LENGTH_SHORT).show();
                                                found = true;
                                                break;
                                            }
                                        }

                                    } catch (JSONException ex) {
                                        ex.printStackTrace();
                                    }
                                }
                                if (found == false) {
                                    friends.put(newFriend);
                                    currentUser2.setFriends(friends);
                                    currentUser2.saveInBackground();
                                    Toast.makeText(FriendsActivity.this, "Friend added!", Toast.LENGTH_SHORT).show();
                                    etAddFriend.setText("");
                                    getFriendUsernames();
                                }
                            } else {
                                Toast.makeText(FriendsActivity.this, "User not found!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        btnAddCloseFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etAddCloseFriend.getText().toString();
                if (currentUser.getUsername().equals(username)) {
                    Toast.makeText(FriendsActivity.this, "You cannot add yourself!", Toast.LENGTH_SHORT).show();
                } else {
                    ParseQuery<User> query = new ParseQuery(User.class);
                    query.whereEqualTo("username", username);
                    query.findInBackground(new FindCallback<User>() {
                        @Override
                        public void done(List<User> objects, ParseException e) {
                            boolean found = false;
                            if (objects.size() == 1) {
                                User newCloseFriend = objects.get(0);
                                String nfID = newCloseFriend.getObjectId();
                                for (int i = 0; i < closeFriends.length(); i++) {
                                    try {
                                        if (closeFriends.get(i).getClass().equals(User.class)) {
                                            Log.i(TAG, "class issue");
                                            User a = (User) closeFriends.get(i);
                                            String uID = a.getObjectId();
                                            if (nfID.equals(uID)) {
                                                found = true;
                                                Toast.makeText(FriendsActivity.this, "This user is already in your close friends list!", Toast.LENGTH_SHORT).show();
                                            }
                                        } else {
                                            JSONObject a = (JSONObject) closeFriends.get(i);
                                            String id = a.getString("objectId");
                                            Log.i(TAG, "this HeLLO " + closeFriends);
                                            if (nfID.equals(id)) {
                                                Toast.makeText(FriendsActivity.this, "This user is already in your close friends list!", Toast.LENGTH_SHORT).show();
                                                found = true;
                                                break;
                                            }
                                        }

                                    } catch (JSONException ex) {
                                        ex.printStackTrace();
                                    }
                                }
                                for (int i = 0; i < friends.length(); i++) {
                                    try {
                                        if (friends.get(i).getClass().equals(User.class)) {
                                            Log.i(TAG, "class issue");
                                            User a = (User) friends.get(i);
                                            String uID = a.getObjectId();
                                            if (nfID.equals(uID)) {
                                                found = true;
                                                Toast.makeText(FriendsActivity.this, "This user is already in your friends list!", Toast.LENGTH_SHORT).show();
                                            }
                                        } else {
                                            JSONObject a = (JSONObject) friends.get(i);
                                            String id = a.getString("objectId");
                                            Log.i(TAG, "this HeLLO " + friends);
                                            if (nfID.equals(id)) {
                                                Toast.makeText(FriendsActivity.this, "This user is already in your friends list!", Toast.LENGTH_SHORT).show();
                                                found = true;
                                                break;
                                            }
                                        }

                                    } catch (JSONException ex) {
                                        ex.printStackTrace();
                                    }
                                }
                                if (found == false) {
                                    closeFriends.put(newCloseFriend);
                                    currentUser2.setCloseFriends(closeFriends);
                                    currentUser2.saveInBackground();
                                    Toast.makeText(FriendsActivity.this, "Close friend added!", Toast.LENGTH_SHORT).show();
                                    etAddCloseFriend.setText("");
                                    getCloseFriendUsernames();
                                }
                            } else {
                                Toast.makeText(FriendsActivity.this, "User not found!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

    }

    private void getCloseFriendUsernames() {
        for (int i = 0; i < closeFriends.length(); i++) {
            Log.i(TAG, "A: " + closeFriendUsernames);
            try {
                if (closeFriends.get(i).getClass().equals(User.class)) {
                    Log.i(TAG, "B: " + closeFriendUsernames);
                    User a = (User) closeFriends.get(i);
                    String currentUsername = a.getUsername();
                    closeFriendUsernames.add(currentUsername);
                    if (closeFriendUsernames.size() == closeFriends.length()) {
                        Log.i(TAG, "CLOSE FRIEND USERNAMES LIST" + closeFriendUsernames);
                        setCloseFriendsCardView();
                    }
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
                            // objects size is always 0
                            if (objects.size() == 1) {
                                User closeFriend = objects.get(0);
                                String currentUsername2 = closeFriend.getUsername();
                                closeFriendUsernames.add(currentUsername2);
                                Log.i(TAG, "AA" + closeFriendUsernames + " " + finalI + " " + (closeFriends.length() - 1));
                                if (closeFriendUsernames.size() == closeFriends.length()) {
                                    Log.i(TAG, "CLOSE FRIEND USERNAMES LIST" + closeFriendUsernames);
                                    setCloseFriendsCardView();
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

    private void getFriendUsernames() {
        for (int i = 0; i < friends.length(); i++) {
            Log.i(TAG, "A: " + friendUsernames);
            try {
                if (friends.get(i).getClass().equals(User.class)) {
                    Log.i(TAG, "B: " + friendUsernames);
                    User a = (User) friends.get(i);
                    String currentUsername = a.getUsername();
                    friendUsernames.add(currentUsername);
                    if (friendUsernames.size() == friends.length()) {
                        Log.i(TAG, "USER FRIEND USERNAMES LIST" + friendUsernames);
                        setFriendsCardView();
                    }
                } else {
                    JSONObject a = (JSONObject) friends.get(i);
                    String temp = a.getString("objectId");
                    Log.i(TAG, "C: " + temp);
                    ParseQuery<User> query = new ParseQuery(User.class);
                    query.whereEqualTo("objectId", temp);
                    int finalI = i;
                    query.findInBackground(new FindCallback<User>() {
                        @Override
                        public void done(List<User> objects, ParseException e) {
                            Log.i(TAG, "D: " + objects);
                            // objects size is always 0
                            if (objects.size() == 1) {
                                User friend = objects.get(0);
                                String currentUsername2 = friend.getUsername();
                                friendUsernames.add(currentUsername2);
                                Log.i(TAG, "AA" + friendUsernames + " " + finalI + " " + (friends.length() - 1));
                                if (friendUsernames.size() == friends.length()) {
                                    Log.i(TAG, "FRIEND USERNAMES LIST" + friendUsernames);
                                    setFriendsCardView();
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

    private void setCloseFriendsCardView() {
        tvCloseFriendsList.setVisibility(View.GONE);
        Log.i(TAG, "aaaaa");
        String text = closeFriendUsernames.toString();
        if (text.contains("[")) {
            text = text.substring(1, text.length());
        }
        if (text.contains("]")) {
            text = text.substring(0, text.length()-1);
        }
        if (text.equals("")){
            text = "No friends added";
        }
        Log.i(TAG, "text" + text);
        tvCloseFriendsList.setText(text);
        tvCloseFriendsList.setVisibility(View.VISIBLE);
    }

    private void setFriendsCardView() {
        tvFriendsList.setVisibility(View.GONE);
        Log.i(TAG, "aaaaa");
        String text = friendUsernames.toString();
        if (text.contains("[")) {
            text = text.substring(1, text.length());
        }
        if (text.contains("]")) {
            text = text.substring(0, text.length()-1);
        }
        if (text.equals("")){
            text = "No friends added";
        }
        Log.i(TAG, "text" + text);
        tvFriendsList.setText(text);
        tvFriendsList.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbarmenu, menu);
        return true;
    }

    public void onLogout(MenuItem item) {
        ParseUser.logOut();
        ParseUser currentUser = ParseUser.getCurrentUser(); // this will now be null
        Intent intent = new Intent(FriendsActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void onHome(MenuItem item) {
        Intent intent = new Intent(FriendsActivity.this, HomeActivity.class);
        startActivity(intent);
        // setVisible(false);
        //finish();
    }

    public void onSettings(MenuItem item) {
        Intent intent = new Intent(FriendsActivity.this, SettingsActivity.class);
        startActivity(intent);
        // setVisible(false);
        //finish();
    }

    public void expandFriendsList(View view) {
        int a = (tvFriendsList.getVisibility() == View.GONE)? View.VISIBLE: View.GONE;

        TransitionManager.beginDelayedTransition(llFriends, new AutoTransition());
        tvFriendsList.setVisibility(a);
    }

    public void expandCloseFriendsList(View view) {
        int a = (tvCloseFriendsList.getVisibility() == View.GONE)? View.VISIBLE: View.GONE;

        TransitionManager.beginDelayedTransition(llCloseFriends, new AutoTransition());
        tvCloseFriendsList.setVisibility(a);
    }
}