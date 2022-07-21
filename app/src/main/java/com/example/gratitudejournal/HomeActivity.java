package com.example.gratitudejournal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.example.myapplication.User;
import com.example.myapplication.Entry;
import com.example.gratitudejournal.Mentions;
import com.parse.SaveCallback;

import org.json.JSONArray;
import org.json.JSONException;

public class HomeActivity extends AppCompatActivity {

    public static final String TAG = "HomeActivity";
    private com.airbnb.lottie.LottieAnimationView avCompose;
    private com.airbnb.lottie.LottieAnimationView avCalendar;
    private com.airbnb.lottie.LottieAnimationView avFriends;
    private com.airbnb.lottie.LottieAnimationView avStats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        avCompose = findViewById(R.id.avCompose);
        avCalendar = findViewById(R.id.avCalendar);
        avFriends = findViewById(R.id.avFriends);
        avStats = findViewById(R.id.avStats);

        ConstraintLayout cl = findViewById(R.id.cl);
        cl.setBackgroundResource(R.color.warm);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.new_color)));

        avCompose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick compose animation");
                Intent i = new Intent (HomeActivity.this, MoodActivity.class);
                startActivity(i);
                //finish();
            }
        });

        avCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick calendar animation");
                Intent i = new Intent (HomeActivity.this, CalendarActivity.class);
                startActivity(i);
                //finish();
            }
        });

        avFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick friends animation");
                Intent i = new Intent (HomeActivity.this, FriendsActivity.class);
                startActivity(i);
                //finish();
            }
        });

        avStats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick stats animation");
                Intent i = new Intent (HomeActivity.this, StatsActivity.class);
                startActivity(i);
                //finish();
            }
        });

        Calendar calendar = Calendar.getInstance();
        calendar.set(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH),
                23, //23
                59, //59
                0
        );
        setAlarm(calendar.getTimeInMillis());

    }

    private void setAlarm(long timeInMillis) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, Alarm.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, timeInMillis, AlarmManager.INTERVAL_DAY, pendingIntent);
        Log.i(TAG, "alarm is set");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        ParseUser currentUser = ParseUser.getCurrentUser();
        //ParseUser currentUser2 = (User) currentUser;
        boolean friendNotifs = ((User) currentUser).getFriendMentions();
        boolean closeFriendNotifs = ((User) currentUser).getCloseFriendMentions();
        if (friendNotifs || closeFriendNotifs) {
            ParseQuery<Mentions> query = new ParseQuery(Mentions.class);
            query.whereEqualTo("toUser", currentUser.getUsername());
            query.findInBackground(new FindCallback<Mentions>() {
                @Override
                public void done(List<Mentions> objects, ParseException e) {
                    if (objects.size() > 0) {
                        menu.findItem(R.id.mentions).setIcon(R.drawable.ic_baseline_notifications_active_24);
                    }
                }
            });
        }


        getMenuInflater().inflate(R.menu.actionbarmenu, menu);
        // menu.findItem(R.id.home).setVisible(Visibility.GONE);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.home).setVisible(false);

        return super.onPrepareOptionsMenu(menu);
    }

    public void onLogout(MenuItem item) {
        ParseUser.logOut();
        ParseUser currentUser = ParseUser.getCurrentUser(); // this will now be null
        Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void onHome(MenuItem item) {
        Intent intent = new Intent(HomeActivity.this, HomeActivity.class);
        startActivity(intent);
        // setVisible(false);
        //finish();
    }

    public void onSettings(MenuItem item) {
        Intent intent = new Intent(HomeActivity.this, SettingsActivity.class);
        startActivity(intent);
        //finish();
    }

    public void onMentions(MenuItem item) {
        Intent intent = new Intent(HomeActivity.this, ViewMentionsActivity.class);
        startActivity(intent);
    }

}
