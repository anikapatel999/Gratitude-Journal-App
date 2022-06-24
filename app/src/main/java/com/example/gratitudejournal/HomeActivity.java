package com.example.gratitudejournal;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.ParseUser;

import java.util.Calendar;

public class HomeActivity extends AppCompatActivity {

    public static final String TAG = "HomeActivity";
    private com.airbnb.lottie.LottieAnimationView avCompose;
    private com.airbnb.lottie.LottieAnimationView avCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        avCompose = findViewById(R.id.avCompose);
        avCalendar = findViewById(R.id.avCalendar);
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
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, timeInMillis, AlarmManager.INTERVAL_DAY, pendingIntent);
        Log.i(TAG, "alarm is set");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbarmenu, menu);
        return true;
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
        setVisible(false);
        //finish();
    }

    public void onSettings(MenuItem item) {
        Intent intent = new Intent(HomeActivity.this, SettingsActivity.class);
        startActivity(intent);
        setVisible(false);
        //finish();
    }

}