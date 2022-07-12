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
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MentionFriendsActivity extends AppCompatActivity {

    public static final String TAG = "MentionFriendsActivity";

    float x1, x2, y1, y2;
    LinearLayout llFriends;
    TextView tvFriendsCardviewText;
    TextView tvFriendsList;

    LinearLayout llCloseFriends;
    TextView tvCloseFriendsCardviewText;
    TextView tvCloseFriendsList;

    EditText etFriends;
    EditText  etCloseFriends;

    Button btnSave;

    JSONArray friends = new JSONArray();
    JSONArray closeFriends = new JSONArray();

    ArrayList<String> friendUsernames = new ArrayList<>();
    ArrayList<String> closeFriendUsernames = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mention_friends);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.new_color)));

        llFriends = findViewById(R.id.llFriends);
        tvFriendsCardviewText = findViewById(R.id.tvFriendsCardviewText);
        tvFriendsList = findViewById(R.id.tvFriendsList);
        llFriends.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);

        llCloseFriends = findViewById(R.id.llCloseFriends);
        tvCloseFriendsCardviewText = findViewById(R.id.tvCloseFriendsCardviewText);
        tvCloseFriendsList = findViewById(R.id.tvCloseFriendsList);
        llCloseFriends.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);

        etFriends = findViewById(R.id.etFriends);
        etCloseFriends = findViewById(R.id.etCloseFriends);

        btnSave = findViewById(R.id.btnSave);

        // set up the cardviews
        ParseUser currentUser = ParseUser.getCurrentUser();
        com.example.myapplication.User currentUser2 = (com.example.myapplication.User) currentUser;
        friends = currentUser2.getFriends();
        closeFriends = currentUser2.getCloseFriends();
        getFriendUsernames();
        getCloseFriendUsernames();

        // TODO: when the user saves the friends / close friends to mention, for each category:
        //  1. check if either section is empty. If it's not empty, call methods that do the following:
        //      1. split the usernames entered and put them into a list.
        //      2. check if list.length() < 3
        //      3. check if the usernames are actually in the list of friend/close friend usernames
        //          (if not, send a toast that "username" is not a friend/close friend)
        //      4. if they are, update the mentions columns of the entry's object in parse
        //          (add another column so you can differentiate between friends and close friends)
        //          and honestly just add the usernames to the array, don't mess with pointers
        //          thats unnecessarily messy and takes too many queries - you can just
        //          query for whereEqualsTo the username
        //      5. Load the mentions into the editTexts upon opening the activity

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputFriends = etFriends.getText().toString();
                String inputCloseFriends = etCloseFriends.getText().toString();
                ArrayList<String> friendMentions = new ArrayList<>();
                ArrayList<String> closeFriendMentions = new ArrayList<>();

                if (!inputFriends.equals("")) {
                    // call method that puts usernames into a list (splits it at the ", "
                    friendMentions = getFriendsArray(inputFriends);
                    if (friendMentions.size() >= 3) {
                        Toast.makeText(MentionFriendsActivity.this, "You can only mention up to 3 friends", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        // check that each element in friendMentions is in friendUsernames
                    }

                }

                if (!inputCloseFriends.equals("")) {
                    // call method that puts usernames into a list (splits it at the ", "
                    closeFriendMentions = getCloseFriendsArray(inputCloseFriends);
                    if (closeFriendMentions.size() >= 3) {
                        Toast.makeText(MentionFriendsActivity.this, "You can only mention up to 3 close friends", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        // check that each element in closeFriendMentions is in closeFriendUsernames
                    }

                }

            }
        });

    }

    private ArrayList<String> getCloseFriendsArray(String inputCloseFriends) {
        ArrayList<String> array = new ArrayList<>();
        String[] elements = inputCloseFriends.split(",");
        for (int i = 0; i < elements.length; i++){
            if(elements[i].contains(" ")) {
                elements[i] = elements[i].substring(1);
            }
            //Log.i(TAG, elements[i]);
            array.add(elements[i]);
        }
        return array;
    }

    private ArrayList<String> getFriendsArray(String inputFriends) {
        ArrayList<String> array = new ArrayList<>();
        String[] elements = inputFriends.split(",");
        for (int i = 0; i < elements.length; i++){
            if(elements[i].contains(" ")) {
                elements[i] = elements[i].substring(1);
            }
            //Log.i(TAG, elements[i]);
            array.add(elements[i]);
        }
        return array;
    }

    private void getCloseFriendUsernames() {
        for (int i = 0; i < closeFriends.length(); i++) {
            Log.i(TAG, "A: " + closeFriendUsernames);
            try {
                if (closeFriends.get(i).getClass().equals(com.example.myapplication.User.class)) {
                    Log.i(TAG, "B: " + closeFriendUsernames);
                    com.example.myapplication.User a = (com.example.myapplication.User) closeFriends.get(i);
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
                    ParseQuery<com.example.myapplication.User> query2 = new ParseQuery(com.example.myapplication.User.class);
                    query2.whereEqualTo("objectId", temp);
                    int finalI = i;
                    query2.findInBackground(new FindCallback<com.example.myapplication.User>() {
                        @Override
                        public void done(List<com.example.myapplication.User> objects, ParseException e) {
                            Log.i(TAG, "D: " + objects);
                            // objects size is always 0
                            if (objects.size() == 1) {
                                com.example.myapplication.User closeFriend = objects.get(0);
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
        Log.i(TAG, "called");
        for (int i = 0; i < friends.length(); i++) {
            Log.i(TAG, "Aaay: " + friendUsernames);
            try {
                if (friends.get(i).getClass().equals(com.example.myapplication.User.class)) {
                    Log.i(TAG, "Bee: " + friendUsernames);
                    com.example.myapplication.User a = (com.example.myapplication.User) friends.get(i);
                    String currentUsername = a.getUsername();
                    friendUsernames.add(currentUsername);
                    if (friendUsernames.size() == friends.length()) {
                        Log.i(TAG, "USER FRIEND USERNAMES LIST" + friendUsernames);
                        setFriendsCardView();
                    }
                } else {
                    JSONObject a = (JSONObject) friends.get(i);
                    String temp = a.getString("objectId");
                    Log.i(TAG, "Cee: " + temp);
                    ParseQuery<com.example.myapplication.User> query = new ParseQuery(com.example.myapplication.User.class);
                    query.whereEqualTo("objectId", temp);
                    int finalI = i;
                    query.findInBackground(new FindCallback<com.example.myapplication.User>() {
                        @Override
                        public void done(List<com.example.myapplication.User> objects, ParseException e) {
                            Log.i(TAG, "Dee: " + objects);
                            // objects size is always 0
                            if (objects.size() == 1) {
                                com.example.myapplication.User friend = objects.get(0);
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
        //TODO: ADJUST HEIGHT SO ALL TEXT FITS or make it scrollable
        //tvCloseFriendsList.setVisibility(View.VISIBLE);
    }

    private void setFriendsCardView() {
        tvFriendsList.setVisibility(View.GONE);
        Log.i(TAG, "friends aaaaa");
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
        //TODO: ADJUST HEIGHT SO ALL TEXT FITS or make it scrollable?
        // tvFriendsList.setVisibility(View.VISIBLE);
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
                    Intent i = new Intent(MentionFriendsActivity.this, QuoteActivity.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    Log.i("swiped left", "it worked");
                    //finish();
                }
                break;
        }
        return false;
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbarmenu, menu);
        return true;
    }

    public void onLogout(MenuItem item) {
        ParseUser.logOut();
        ParseUser currentUser = ParseUser.getCurrentUser(); // this will now be null
        Intent intent = new Intent(MentionFriendsActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void onHome(MenuItem item) {
        Intent intent = new Intent(MentionFriendsActivity.this, HomeActivity.class);
        startActivity(intent);
        // finish();
    }

    public void onSettings(MenuItem item) {
        Intent intent = new Intent(MentionFriendsActivity.this, SettingsActivity.class);
        startActivity(intent);
        // setVisible(false);
        //finish();
    }
}