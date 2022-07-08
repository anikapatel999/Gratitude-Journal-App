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

        //FOR ADDING FRIENDS
        btnAddFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etAddFriend.getText().toString();
                ParseQuery<User> query = new ParseQuery(User.class);
                query.whereEqualTo("username", username);
                query.findInBackground(new FindCallback<User>() {
                    @Override
                    public void done(List<User> objects, ParseException e) {
                        boolean found = false;
                        if (objects.size() == 1){
                            User newFriend = objects.get(0);
                            String nfID = newFriend.getObjectId();
                            for (int i = 0; i < friends.length(); i++) {
                                try {
                                    if (friends.get(i).getClass().equals(User.class)){
                                        Log.i(TAG, "class issue");
                                        User a = (User) friends.get(i);
                                        String uID = a.getObjectId();
                                        if(nfID.equals(uID)) {
                                            found = true;
                                            Toast.makeText(FriendsActivity.this, "This user is already in your friends list!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    else {
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
                                    if (closeFriends.get(i).getClass().equals(User.class)){
                                        Log.i(TAG, "class issue");
                                        User a = (User) closeFriends.get(i);
                                        String uID = a.getObjectId();
                                        if(nfID.equals(uID)) {
                                            found = true;
                                            Toast.makeText(FriendsActivity.this, "This user is already in your close friends list!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    else {
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
                            }
                        }
                        else {
                            Toast.makeText(FriendsActivity.this, "User not found!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });


        btnAddCloseFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etAddCloseFriend.getText().toString();
                ParseQuery<User> query = new ParseQuery(User.class);
                query.whereEqualTo("username", username);
                query.findInBackground(new FindCallback<User>() {
                    @Override
                    public void done(List<User> objects, ParseException e) {
                        boolean found = false;
                        if (objects.size() == 1){
                            User newCloseFriend = objects.get(0);
                            String nfID = newCloseFriend.getObjectId();
                            for (int i = 0; i < closeFriends.length(); i++) {
                                try {
                                    if (closeFriends.get(i).getClass().equals(User.class)){
                                        Log.i(TAG, "class issue");
                                        User a = (User) closeFriends.get(i);
                                        String uID = a.getObjectId();
                                        if(nfID.equals(uID)) {
                                            found = true;
                                            Toast.makeText(FriendsActivity.this, "This user is already in your close friends list!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    else {
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
                                    if (friends.get(i).getClass().equals(User.class)){
                                        Log.i(TAG, "class issue");
                                        User a = (User) friends.get(i);
                                        String uID = a.getObjectId();
                                        if(nfID.equals(uID)) {
                                            found = true;
                                            Toast.makeText(FriendsActivity.this, "This user is already in your friends list!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    else {
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
                            }
                        }
                        else {
                            Toast.makeText(FriendsActivity.this, "User not found!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });


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