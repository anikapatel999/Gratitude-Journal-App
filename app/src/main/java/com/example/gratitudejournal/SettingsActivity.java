package com.example.gratitudejournal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.new_color)));

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

        ParseUser currentUser = ParseUser.getCurrentUser();
        User currentUser2 = (User) currentUser;
        if (currentUser2.getTimeZone() != null) {
            String currentTimezone = currentUser2.getTimeZone();
            int ind = findIndex(currentTimezone);
            sTimeZoneDropdown.setSelection(ind);
        }
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

    public int findIndex(String timezone) {
            if(timezone.equals("ACT")) { return 0; }
            if(timezone.equals("AET")) { return 1; }
            if(timezone.equals("AGT")) { return 2; }
            if(timezone.equals("ART")) { return 3; }
            if(timezone.equals("AST")) { return 4; }
            if(timezone.equals("BET")) { return 5; }
            if(timezone.equals("BST")) { return 6; }
            if(timezone.equals("CAT")) { return 7; }
            if(timezone.equals("CNT")) { return 8; }
            if(timezone.equals("CST")) { return 9; }
            if(timezone.equals("CTT")) { return 10; }
            if(timezone.equals("EAT")) { return 11; }
            if(timezone.equals("ECT")) { return 12; }
            if(timezone.equals("EET")) { return 13; }
            if(timezone.equals("EST")) { return 14; }
            if(timezone.equals("GMT")) { return 15; }
            if(timezone.equals("HST")) { return 16; }
            if(timezone.equals("IET")) { return 17; }
            if(timezone.equals("IST")) { return 18; }
            if(timezone.equals("JST")) { return 19; }
            if(timezone.equals("MET")) { return 20; }
            if(timezone.equals("MIT")) { return 21; }
            if(timezone.equals("MST")) { return 22; }
            if(timezone.equals("NET")) { return 23; }
            if(timezone.equals("NST")) { return 24; }
            if(timezone.equals("PLT")) { return 25; }
            if(timezone.equals("PNT")) { return 26; }
            if(timezone.equals("PRT")) { return 27; }
            if(timezone.equals("PST")) { return 28; }
            if(timezone.equals("SST")) { return 29; }
            if(timezone.equals("UTC")) { return 30; }
            if(timezone.equals("VST")) { return 31; }
            return 0;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbarmenu, menu);
        menu.findItem(R.id.settings).setVisible(false);
        return true;
    }

    public void onLogout(MenuItem item) {
        ParseUser.logOut();
        ParseUser currentUser = ParseUser.getCurrentUser(); // this will now be null
        Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void onHome(MenuItem item) {
        Intent intent = new Intent(SettingsActivity.this, HomeActivity.class);
        startActivity(intent);
        // finish();
    }

    public void onSettings(MenuItem item) {
        Intent intent = new Intent(SettingsActivity.this, SettingsActivity.class);
        startActivity(intent);
        // setVisible(false);
        //finish();
    }
}