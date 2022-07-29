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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gratitudejournal.R;
import com.google.android.material.snackbar.Snackbar;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.example.gratitudejournal.parse.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import com.example.gratitudejournal.parse.Entry;

public class FriendsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    public static final String TAG = "FriendsActivity";

    ConstraintLayout cl;

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

        cl = findViewById(R.id.cl);

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

        ConstraintLayout cl = findViewById(R.id.cl);
        cl.setBackgroundResource(R.color.warm);

        ParseUser currentUser = ParseUser.getCurrentUser();
        User currentUser2 = (User) currentUser;
        friends = currentUser2.getFriends();
        closeFriends = currentUser2.getCloseFriends();

        sRemoveFriend.setOnItemSelectedListener(this);
        sRemoveCloseFriend.setOnItemSelectedListener(this);

        // Make list of friend usernames
        getFriendUsernames();

        // Make list of close friend usernames
        getCloseFriendUsernames();

        btnAddFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etAddFriend.getText().toString();
                // check that the user isn't trying to add themselves
                if (currentUser.getUsername().equals(username)) {
                    Toast.makeText(FriendsActivity.this, "You cannot add yourself!", Toast.LENGTH_SHORT).show();
                } else {
                    // make a query that searches for a user with a username that
                    // matches the one the current user entered
                    ParseQuery<User> query = new ParseQuery(User.class);
                    query.whereEqualTo("username", username);
                    query.findInBackground(new FindCallback<User>() {
                        @Override
                        public void done(List<User> objects, ParseException e) {
                            boolean found = false;
                            boolean found2 = false;
                            if (objects.size() == 1) {
                                User newFriend = objects.get(0);
                                String nfID = newFriend.getObjectId();

                                found = inCloseFriendsList(nfID);
                                if (found) {
                                    Toast.makeText(FriendsActivity.this, "This user is already in your close friends list!", Toast.LENGTH_SHORT).show();
                                }

                                found2 = inFriendsList(nfID);
                                if (found2) {
                                    Toast.makeText(FriendsActivity.this, "This user is already in your friends list!", Toast.LENGTH_SHORT).show();
                                }
                                // if a user with the username exists and is not already added to a list
                                if (found == false && found2 == false) {
                                    friends.put(newFriend);
                                    currentUser2.setFriends(friends);
                                    currentUser2.saveInBackground();
                                    Toast.makeText(FriendsActivity.this, "Friend added!", Toast.LENGTH_SHORT).show();
                                    etAddFriend.setText("");
                                    //getFriendUsernames();
                                    recreate();
                                }
                                // if a user with the username does not exist
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
                            boolean found2 = false;
                            if (objects.size() == 1) {
                                User newCloseFriend = objects.get(0);
                                String nfID = newCloseFriend.getObjectId();

                                found = inCloseFriendsList(nfID);
                                if (found) {
                                    Toast.makeText(FriendsActivity.this, "This user is already in your close friends list!", Toast.LENGTH_SHORT).show();
                                }

                                found2 = inFriendsList(nfID);

                                if (found == false && found2 == false) {
                                    closeFriends.put(newCloseFriend);
                                    currentUser2.setCloseFriends(closeFriends);
                                    currentUser2.saveInBackground();
                                    Toast.makeText(FriendsActivity.this, "Close friend added!", Toast.LENGTH_SHORT).show();
                                    etAddCloseFriend.setText("");
                                    recreate();
                                }
                                else if (found == false && found2 == true) {
                                    int ind = 0;
                                    for (int j = 0; j < friendUsernames.size(); j++) {
                                        if (friendUsernames.get(j).equals(username)) {
                                            ind = j;
                                            Log.i(TAG, "AAAAA " + friendUsernames.get(j));
                                            break;
                                        }
                                    }
                                    Snackbar mySnackbar = Snackbar.make(cl,
                                            "Promote friend to close friend?", Snackbar.LENGTH_LONG);
                                    int finalInd = ind;
                                    mySnackbar.setAction("yes", new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    closeFriends.put(newCloseFriend);
                                                    currentUser2.setCloseFriends(closeFriends);
                                                    currentUser2.saveInBackground();
                                                    Toast.makeText(FriendsActivity.this, "Close friend added!", Toast.LENGTH_SHORT).show();
                                                    etAddCloseFriend.setText("");
                                                    removeFriend(finalInd +1);
                                                    recreate();
                                                }
                                            });
                                            mySnackbar.show();
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

    private boolean inFriendsList(String nfID) {
        boolean found = false;
        for (int i = 0; i < friends.length(); i++) {
            try {
                if (friends.get(i).getClass().equals(User.class)) {
                    Log.i(TAG, "class issue");
                    User a = (User) friends.get(i);
                    String uID = a.getObjectId();
                    if (nfID.equals(uID)) {
                        found = true;
                    }
                } else {
                    JSONObject a = (JSONObject) friends.get(i);
                    String id = a.getString("objectId");
                    Log.i(TAG, "this HeLLO " + friends);
                    if (nfID.equals(id)) {
                        found = true;
                        break;
                    }
                }

            } catch (JSONException ex) {
                ex.printStackTrace();
            }
        }
        return found;
    }

    private boolean inCloseFriendsList(String nfID) {
        boolean found = false;
        for (int i = 0; i < closeFriends.length(); i++) {
            try {
                if (closeFriends.get(i).getClass().equals(User.class)) {
                    Log.i(TAG, "class issue");
                    User a = (User) closeFriends.get(i);
                    String uID = a.getObjectId();
                    if (nfID.equals(uID)) {
                        found = true;
                        break;
                    }
                } else {
                    JSONObject a = (JSONObject) closeFriends.get(i);
                    String id = a.getString("objectId");
                    Log.i(TAG, "this HeLLO " + closeFriends);
                    if (nfID.equals(id)) {
                        found = true;
                        break;
                    }
                }

            } catch (JSONException ex) {
                ex.printStackTrace();
            }
        }
        return found;
    }

    private void getCloseFriendUsernames() {
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
                        setCloseFriendsCardView();
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
                                // call setCloseFriendsCardView()
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
                        setFriendsCardView();
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
                                // call setFriendsCardView()
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
        setRemoveCloseFriends();
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
        setRemoveFriends();
    }

    private void setRemoveCloseFriends() {
        String[] closeFriendArray = new String[closeFriendUsernames.size()+1];
        closeFriendArray[0] = "---";
        for (int i = 1; i < closeFriendUsernames.size() + 1; i++){
            closeFriendArray[i] = closeFriendUsernames.get(i-1);
        }
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, closeFriendArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sRemoveCloseFriend.setAdapter(adapter);
    }


    private void setRemoveFriends() {
        String[] friendArray = new String[friendUsernames.size()+1];
        friendArray[0] = "---";
        for (int i = 1; i < friendUsernames.size() + 1; i++){
            friendArray[i] = friendUsernames.get(i-1);
        }
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, friendArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sRemoveFriend.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbarmenu, menu);
        menu.findItem(R.id.mentions).setVisible(false);
        return true;
    }

    public void onMentions(MenuItem item) {
        Intent intent = new Intent(FriendsActivity.this, ViewMentionsActivity.class);
        startActivity(intent);
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //TODO: IMPLEMENT
        Log.i(TAG, "oneee: " + parent.getId());
        Log.i(TAG, "oneee: " + view);

        if(position != 0) {
            if (parent.getId() == R.id.sRemoveFriend) {
                Log.i(TAG, "yes!!");
                removeFriend(position);
            }
            if (parent.getId() == R.id.sRemoveCloseFriend) {
                Log.i(TAG, "no!!");
                removeCloseFriend(position);
            }
        }
    }

    private void removeCloseFriend(int position) {
        ParseUser cu = ParseUser.getCurrentUser();
        User cu2 = (User) cu;
        JSONArray mentionedCloseFriends = new JSONArray();

        if (cu2.getCurrentEntry() != null) {
            Entry lastEntry = cu2.getCurrentEntry();
            try {
                mentionedCloseFriends = lastEntry.fetchIfNeeded().getJSONArray("closeFriendMentions");
                Log.i(TAG, String.valueOf(mentionedCloseFriends));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }


        String closeFriendToRemove = closeFriendUsernames.get(position-1);
        ParseQuery<User> query = new ParseQuery(User.class);
        query.whereEqualTo("username", closeFriendToRemove);
        JSONArray finalMentionedCloseFriends = mentionedCloseFriends;
        JSONArray finalMentionedCloseFriends1 = mentionedCloseFriends;
        query.findInBackground(new FindCallback<User>() {
            @Override
            public void done(List<User> objects, ParseException e) {
                if (objects.size() == 1) {
                    User goneCloseFriend = objects.get(0);
                    String nfID = goneCloseFriend.getObjectId();
                    for (int i = 0; i < closeFriends.length(); i++) {
                        try {
                            if (closeFriends.get(i).getClass().equals(User.class)) {
                                Log.i(TAG, "class issue");
                                User a = (User) closeFriends.get(i);
                                String uID = a.getObjectId();
                                boolean closeFriendMentioned = false;
                                for (int u = 0; u < finalMentionedCloseFriends.length(); u++) {
                                    Log.i(TAG, "got to loop: " + closeFriendToRemove + " " + finalMentionedCloseFriends.get(u));
                                    if (finalMentionedCloseFriends.get(u).toString().equals(closeFriendToRemove)) {
                                        Log.i(TAG, "friend already mentioned");
                                        Toast.makeText(FriendsActivity.this, "This close friend is already mentioned in your journal entry. Please remove the mention and try again", Toast.LENGTH_LONG).show();
                                        closeFriendMentioned = true;
                                        break;
                                    }
                                }
                                if (nfID.equals(uID) && !closeFriendMentioned) {
                                    closeFriends.remove(i);
                                    Toast.makeText(FriendsActivity.this, "Close friend removed!", Toast.LENGTH_SHORT).show();
                                    ParseUser currentUser = ParseUser.getCurrentUser();
                                    User currentUser2 = (User) currentUser;
                                    currentUser2.setCloseFriends(closeFriends);
                                    currentUser2.saveInBackground();
                                    recreate();
                                    break;
                                }
                            } else {
                                JSONObject a = (JSONObject) closeFriends.get(i);
                                String id = a.getString("objectId");
                                Log.i(TAG, "this HeLLO " + closeFriends);
                                boolean closeFriendMentioned = false;
                                for (int u = 0; u < finalMentionedCloseFriends1.length(); u++) {
                                    Log.i(TAG, "got to loop: " + closeFriendToRemove + " " + finalMentionedCloseFriends1.get(u));
                                    if (finalMentionedCloseFriends1.get(u).toString().equals(closeFriendToRemove)) {
                                        Log.i(TAG, "friend already mentioned");
                                        Toast.makeText(FriendsActivity.this, "This close friend is already mentioned in your journal entry. Please remove the mention and try again", Toast.LENGTH_LONG).show();
                                        closeFriendMentioned = true;
                                        break;
                                    }
                                }
                                if (nfID.equals(id) && !closeFriendMentioned) {
                                    closeFriends.remove(i);
                                    Toast.makeText(FriendsActivity.this, "close Friend removed!", Toast.LENGTH_SHORT).show();
                                    ParseUser currentUser = ParseUser.getCurrentUser();
                                    User currentUser2 = (User) currentUser;
                                    currentUser2.setCloseFriends(closeFriends);
                                    currentUser2.saveInBackground();
                                    recreate();
                                    break;
                                }
                            }

                        } catch (JSONException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }
        });

    }

    private void removeFriend(int position) {
        ParseUser cu = ParseUser.getCurrentUser();
        User cu2 = (User) cu;
        JSONArray mentionedFriends = new JSONArray();

        if (cu2.getCurrentEntry() != null) {
            Entry lastEntry = cu2.getCurrentEntry();
            try {
                mentionedFriends = lastEntry.fetchIfNeeded().getJSONArray("friendMentions");
                Log.i(TAG, String.valueOf(mentionedFriends));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        String friendToRemove = friendUsernames.get(position-1);
        ParseQuery<User> query = new ParseQuery(User.class);
        query.whereEqualTo("username", friendToRemove);
        JSONArray finalMentionedFriends = mentionedFriends;
        JSONArray finalMentionedFriends1 = mentionedFriends;
        query.findInBackground(new FindCallback<User>() {
            @Override
            public void done(List<User> objects, ParseException e) {
                if (objects.size() == 1) {
                    User goneFriend = objects.get(0);
                    String nfID = goneFriend.getObjectId();
                    for (int i = 0; i < friends.length(); i++) {
                        try {
                            if (friends.get(i).getClass().equals(User.class)) {
                                Log.i(TAG, "class issue");
                                User a = (User) friends.get(i);
                                String uID = a.getObjectId();
                                boolean friendMentioned = false;
                                for (int u = 0; u < finalMentionedFriends.length(); u++) {
                                    Log.i(TAG, "got to loop: " + friendToRemove + " " + finalMentionedFriends.get(u));
                                    if (finalMentionedFriends.get(u).toString().equals(friendToRemove)) {
                                        Log.i(TAG, "friend already mentioned");
                                        Toast.makeText(FriendsActivity.this, "This friend is already mentioned in your journal entry. Please remove the mention and try again", Toast.LENGTH_LONG).show();
                                        friendMentioned = true;
                                        break;
                                    }
                                }
                                if (nfID.equals(uID) && !friendMentioned) {
                                    friends.remove(i);
                                    Toast.makeText(FriendsActivity.this, "Friend removed!", Toast.LENGTH_SHORT).show();
                                    ParseUser currentUser = ParseUser.getCurrentUser();
                                    User currentUser2 = (User) currentUser;
                                    currentUser2.setFriends(friends);
                                    currentUser2.saveInBackground();
                                    recreate();
                                    break;
                                }
                            } else {
                                JSONObject a = (JSONObject) friends.get(i);
                                String id = a.getString("objectId");
                                Log.i(TAG, "this HeLLO " + friends);
                                boolean friendMentioned = false;

                                for (int u = 0; u < finalMentionedFriends1.length(); u++) {
                                    Log.i(TAG, "got to loop: " + friendToRemove + " " + finalMentionedFriends1.get(u));
                                    if (finalMentionedFriends1.get(u).toString().equals(friendToRemove)) {
                                        Log.i(TAG, "friend already mentioned");
                                        Toast.makeText(FriendsActivity.this, "This friend is already mentioned in your journal entry. Please remove the mention and try again", Toast.LENGTH_LONG).show();
                                        friendMentioned = true;
                                        break;
                                    }
                                }
                                if (nfID.equals(id) && !friendMentioned) {
                                    friends.remove(i);
                                    Toast.makeText(FriendsActivity.this, "Friend removed!", Toast.LENGTH_SHORT).show();
                                    ParseUser currentUser = ParseUser.getCurrentUser();
                                    User currentUser2 = (User) currentUser;
                                    currentUser2.setFriends(friends);
                                    currentUser2.saveInBackground();
                                    recreate();
                                    break;
                                }
                            }

                        } catch (JSONException ex) {
                            ex.printStackTrace();
                        }
                    }
            }
        }
    });
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // nothing needs to be done
    }
}