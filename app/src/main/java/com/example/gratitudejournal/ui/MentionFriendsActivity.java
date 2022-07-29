package com.example.gratitudejournal.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

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

import com.example.gratitudejournal.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import com.example.gratitudejournal.parse.Entry;
import com.example.gratitudejournal.parse.User;

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

        ConstraintLayout cl = findViewById(R.id.cl);
        cl.setBackgroundResource(R.color.warm);

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
        User currentUser2 = (User) currentUser;
        Entry entry = currentUser2.getCurrentEntry();
        friends = currentUser2.getFriends();
        closeFriends = currentUser2.getCloseFriends();
        getFriendUsernames();
        getCloseFriendUsernames();
        setEtFriends(entry.getFriendMentions());
        setEtCloseFriends(entry.getCloseFriendMentions());


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputFriends = etFriends.getText().toString();
                String inputCloseFriends = etCloseFriends.getText().toString();
                ArrayList<String> friendMentions = new ArrayList<>();
                ArrayList<String> closeFriendMentions = new ArrayList<>();

                if (!inputFriends.equals("")) {
                    friendMentions = getFriendsArray(inputFriends);
                    if (friendMentions.size() > 3) {
                        Toast.makeText(MentionFriendsActivity.this, "You can only mention up to 3 friends", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        boolean allMatches = true;
                        HashSet<String> friendUsernamesSet = new HashSet<>(friendUsernames);
                        for (int i = 0; i < friendMentions.size(); i++) {
                            if (!friendUsernamesSet.contains(friendMentions.get(i))) {
                                Toast.makeText(MentionFriendsActivity.this, friendMentions.get(i) + " is not in your friends list", Toast.LENGTH_SHORT).show();
                                allMatches = false;
                            }
                        }

                        if(allMatches) {
                            Log.d(TAG, "all friends mentioned are in the user's friends list");
                            entry.setFriendMentions(friendMentions);
                            try {
                                Toast.makeText(MentionFriendsActivity.this, "Friends saved!", Toast.LENGTH_SHORT).show();
                                entry.save();
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }

                    }

                }

                else {
                    ArrayList<String> temp = new ArrayList<>();
                    entry.setFriendMentions(temp);
                    try {
                        Toast.makeText(MentionFriendsActivity.this, "Friends saved!", Toast.LENGTH_SHORT).show();
                        entry.save();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

                if (!inputCloseFriends.equals("")) {
                    closeFriendMentions = getCloseFriendsArray(inputCloseFriends);
                    if (closeFriendMentions.size() > 3) {
                        Toast.makeText(MentionFriendsActivity.this, "You can only mention up to 3 close friends", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        boolean allMatches = true;
                        HashSet<String> closeFriendUsernamesSet = new HashSet<>(closeFriendUsernames);
                        for (int i = 0; i < closeFriendMentions.size(); i++) {
                            if (!closeFriendUsernamesSet.contains(closeFriendMentions.get(i))) {
                                Toast.makeText(MentionFriendsActivity.this, closeFriendMentions.get(i) + " is not in your close friends list", Toast.LENGTH_SHORT).show();
                                allMatches = false;
                            }
                        }

                        if(allMatches) {
                            Log.d(TAG, "all friends mentioned are in the user's close friends list");
                            entry.setCloseFriendMentions(closeFriendMentions);
                            try {
                                Toast.makeText(MentionFriendsActivity.this, "Close friends saved!", Toast.LENGTH_SHORT).show();
                                entry.save();
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }

                    }

                }
                else {
                    ArrayList<String> temp = new ArrayList<>();
                    entry.setCloseFriendMentions(temp);
                    try {
                        entry.save();
                        Toast.makeText(MentionFriendsActivity.this, "Close friends saved!", Toast.LENGTH_SHORT).show();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

            }
        });

    }

    private void setEtCloseFriends(JSONArray closeFriendMentions) {
        ArrayList<String> fm = new ArrayList<>();
        for(int i = 0; i < closeFriendMentions.length(); i++){
            try {
                fm.add(closeFriendMentions.get(i).toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        String text = fm.toString();
        if (text.contains("[")) {
            text = text.substring(1, text.length());
        }
        if (text.contains("]")) {
            text = text.substring(0, text.length()-1);
        }
        Log.d(TAG, "close friends text: " + text);
        etCloseFriends.setText(text);    }

    private void setEtFriends(JSONArray friendMentions) {
        ArrayList<String> fm = new ArrayList<>();
        for(int i = 0; i < friendMentions.length(); i++){
            try {
                fm.add(friendMentions.get(i).toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        String text = fm.toString();
        if (text.contains("[")) {
            text = text.substring(1, text.length());
        }
        if (text.contains("]")) {
            text = text.substring(0, text.length()-1);
        }
        Log.d(TAG, "friends text: " + text);
        etFriends.setText(text);
    }

    private ArrayList<String> getCloseFriendsArray(String inputCloseFriends) {
        ArrayList<String> array = new ArrayList<>();
        String[] elements = inputCloseFriends.split(",");
        for (int i = 0; i < elements.length; i++){
            if(elements[i].contains(" ")) {
                elements[i] = elements[i].substring(1);
            }
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
            array.add(elements[i]);
        }
        return array;
    }

    private void getCloseFriendUsernames() {
        for (int i = 0; i < closeFriends.length(); i++) {
            Log.d(TAG, "close friend usernames: " + closeFriendUsernames);
            try {
                if (closeFriends.get(i).getClass().equals(User.class)) {
                    User a = (User) closeFriends.get(i);
                    String currentUsername = a.getUsername();
                    closeFriendUsernames.add(currentUsername);
                    if (closeFriendUsernames.size() == closeFriends.length()) {
                        Log.d(TAG, "close friend usernames updated: " + closeFriendUsernames);
                        setCloseFriendsCardView();
                    }
                } else {
                    JSONObject a = (JSONObject) closeFriends.get(i);
                    String temp = a.getString("objectId");
                    Log.d(TAG, "object id: " + temp);
                    ParseQuery<User> query2 = new ParseQuery(User.class);
                    query2.whereEqualTo("objectId", temp);
                    int finalI = i;
                    query2.findInBackground(new FindCallback<User>() {
                        @Override
                        public void done(List<User> objects, ParseException e) {
                            Log.d(TAG, "list from the query: " + objects);
                            if (objects.size() == 1) {
                                User closeFriend = objects.get(0);
                                String currentUsername2 = closeFriend.getUsername();
                                closeFriendUsernames.add(currentUsername2);
                                Log.d(TAG, "current variable values: " + closeFriendUsernames + " " + finalI + " " + (closeFriends.length() - 1));
                                if (closeFriendUsernames.size() == closeFriends.length()) {
                                    Log.d(TAG, "close friend usernames list final: " + closeFriendUsernames);
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
        Log.i(TAG, "getFriendUsernames called");
        for (int i = 0; i < friends.length(); i++) {
            Log.d(TAG, "friend usernames: " + friendUsernames);
            try {
                if (friends.get(i).getClass().equals(User.class)) {
                    User a = (User) friends.get(i);
                    String currentUsername = a.getUsername();
                    friendUsernames.add(currentUsername);
                    if (friendUsernames.size() == friends.length()) {
                        Log.d(TAG, "friend usernames list updated: " + friendUsernames);
                        setFriendsCardView();
                    }
                } else {
                    JSONObject a = (JSONObject) friends.get(i);
                    String temp = a.getString("objectId");
                    Log.d(TAG, "object id: " + temp);
                    ParseQuery<User> query = new ParseQuery(User.class);
                    query.whereEqualTo("objectId", temp);
                    int finalI = i;
                    query.findInBackground(new FindCallback<User>() {
                        @Override
                        public void done(List<User> objects, ParseException e) {
                            Log.d(TAG, "list from the query: " + objects);
                            // objects size is always 0
                            if (objects.size() == 1) {
                                User friend = objects.get(0);
                                String currentUsername2 = friend.getUsername();
                                friendUsernames.add(currentUsername2);
                                Log.d(TAG, "current variable values: " + friendUsernames + " " + finalI + " " + (friends.length() - 1));
                                if (friendUsernames.size() == friends.length()) {
                                    Log.d(TAG, "friend usernames list final: " + friendUsernames);
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
        Log.d(TAG, "close friends card view text: " + text);
        tvCloseFriendsList.setText(text);
    }

    private void setFriendsCardView() {
        tvFriendsList.setVisibility(View.GONE);
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
        Log.d(TAG, "friends card view text: " + text);
        tvFriendsList.setText(text);
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
                    Intent i = new Intent(MentionFriendsActivity.this, QuoteActivity.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    Log.d(TAG, "swiping left worked");
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
        menu.findItem(R.id.mentions).setVisible(false);
        return true;
    }

    public void onMentions(MenuItem item) {
        Intent intent = new Intent(MentionFriendsActivity.this, ViewMentionsActivity.class);
        startActivity(intent);
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
    }

    public void onSettings(MenuItem item) {
        Intent intent = new Intent(MentionFriendsActivity.this, SettingsActivity.class);
        startActivity(intent);

    }
}