package com.example.gratitudejournal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.example.myapplication.User;
import com.parse.SaveCallback;

public class SettingsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    public static final String TAG = "SignupActivity";
    private Spinner sTimeZoneDropdown;
    private ToggleButton tMentionFriends;
    private ToggleButton tFriendMentions;
    private ToggleButton tMentionCloseFriends;
    private ToggleButton tCloseFriendMentions;
    private Button btnDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        sTimeZoneDropdown = findViewById(R.id.sTimeZoneDropdown);
        tMentionFriends = findViewById(R.id.tMentionFriends);
        tFriendMentions = findViewById(R.id.tFriendMentions);
        tMentionCloseFriends = findViewById(R.id.tMentionCloseFriends);
        tCloseFriendMentions = findViewById(R.id.tCloseFriendMentions);
        btnDone = findViewById(R.id.btnDone);

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick done button");
                Intent intent = new Intent(SettingsActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        sTimeZoneDropdown.setOnItemSelectedListener(this);

        String[] timeZoneArray = getResources().getStringArray(R.array.timezones);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, timeZoneArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sTimeZoneDropdown.setAdapter(adapter);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        ParseUser currentUser = ParseUser.getCurrentUser();
        User currentUser2 = (User) currentUser;
        String tz = calcTimeZone(position);
        currentUser2.setTimeZone(tz);
        currentUser2.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                Log.i(TAG, "got to save in background");
                if (e != null) {
                    Log.e(TAG, "Issue with saving", e);
                    Toast.makeText(SettingsActivity.this, "Error while saving!", Toast.LENGTH_SHORT).show();
                }
                Log.i(TAG, "Post save was successful", e);
            }
        });
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        ParseUser currentUser = ParseUser.getCurrentUser();
        User currentUser2 = (User) currentUser;
        currentUser2.setTimeZone("GMT");
        currentUser2.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                Log.i(TAG, "got to save in background");
                if (e != null) {
                    Log.e(TAG, "Issue with saving", e);
                    Toast.makeText(SettingsActivity.this, "Error while saving!", Toast.LENGTH_SHORT).show();
                }
                Log.i(TAG, "Post save was successful", e);
            }
        });
    }

    public String calcTimeZone(int position) {
        if(position == 0) { return "ACT"; }
        if(position == 1) { return "AET"; }
        if(position == 2) { return "AGT"; }
        if(position == 3) { return "ART"; }
        if(position == 4) { return "AST"; }
        if(position == 5) { return "BET"; }
        if(position == 6) { return "BST"; }
        if(position == 7) { return "CAT"; }
        if(position == 8) { return "CNT"; }
        if(position == 9) { return "CST"; }
        if(position == 10) { return "CTT"; }
        if(position == 11) { return "EAT"; }
        if(position == 12) { return "ECT"; }
        if(position == 13) { return "EET"; }
        if(position == 14) { return "EST"; }
        if(position == 15) { return "GMT"; }
        if(position == 16) { return "HST"; }
        if(position == 17) { return "IET"; }
        if(position == 18) { return "IST"; }
        if(position == 19) { return "JST"; }
        if(position == 20) { return "MET"; }
        if(position == 21) { return "MIT"; }
        if(position == 22) { return "MST"; }
        if(position == 23) { return "NET"; }
        if(position == 24) { return "NST"; }
        if(position == 25) { return "PLT"; }
        if(position == 26) { return "PNT"; }
        if(position == 27) { return "PRT"; }
        if(position == 28) { return "PST"; }
        if(position == 29) { return "SST"; }
        if(position == 30) { return "UTC"; }
        if(position == 31) { return "VST"; }

        return "GMT";
    }
}