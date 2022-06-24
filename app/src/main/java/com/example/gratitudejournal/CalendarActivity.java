package com.example.gratitudejournal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CalendarView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.example.myapplication.Entry;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import com.example.myapplication.User;

public class CalendarActivity extends AppCompatActivity {

    public static final String TAG = "CalendarActivity";

    CalendarView cvCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        cvCalendar = (CalendarView) findViewById(R.id.cvCalendar);

        cvCalendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                // TODO: parse uses UTC. to figure out time zone issues:
                //  1. date = the current selected day on the calendar
                //  2. find the current time zone
                //  3. find how many hours we are ahead / behind UTC.
                //      - If there isn't already a method for that, we can make one
                //      - (its just a bunch of if statements)
                //      - call this int timeDifference
                //  4. d1 = date - timeDifference (in ms)
                //  5. d2 = d1 + i day

                ParseUser currentUser = ParseUser.getCurrentUser();
                ParseQuery<Entry> query = new ParseQuery(Entry.class);
                //LocalDate date = LocalDate.of(year, month, dayOfMonth);
                // WHY DOES IT THINK THE YEAR IS 3922?????? (some georgian calendar stuff,
                // starts at 1900 so you have to subtract 1900)
                User currentUser2 = (User) currentUser;
                String timezone = currentUser2.getTimeZone();
                Double timezoneDifference = getTimeDifference(timezone);

                Date date = new Date(year-1900, month, dayOfMonth);
                Date d1 = new Date((long) (date.getTime() - (timezoneDifference * 60 * 60 * 1000)));
                Date d2 = new Date(d1.getTime() + (1 * 25 * 60 * 60 * 1000));
                query.whereGreaterThanOrEqualTo("createdAt", d1);
                query.whereLessThan("createdAt", d2);
                query.whereMatches("user", currentUser.getObjectId());
                Log.i(TAG, "query: " + query);

                query.findInBackground(new FindCallback<Entry>() {
                    @Override
                    public void done(List<Entry> entries, ParseException e) {
                        // check for errors
                        Log.i(TAG, "IT DID SOMETHING" + entries);
                        if (e != null) {
                            Log.e(TAG, "Issue with getting entries", e);
                            return;
                        }
                        // for debugging purposes let's print every post description to logcat
                        if (entries.size() > 0) {
                            Toast.makeText(CalendarActivity.this, "selected date " + month + "/" + dayOfMonth + "/" + year, Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(CalendarActivity.this, ScrollActivity.class);
                            String date2 = (String.valueOf(dayOfMonth) + String.valueOf(month) + String.valueOf(year));
                            intent.putExtra("date", date2);
                            startActivity(intent);
                        }
                        else {
                            Toast.makeText(CalendarActivity.this, "No entry at" + month + "/" + dayOfMonth + "/" + year, Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }

    private Double getTimeDifference(String timezone) {
        if(timezone.equals("ACT")) { return 9.5; }
        if(timezone.equals("AET")) { return 10.0; }
        if(timezone.equals("AGT")) { return -3.0; }
        if(timezone.equals("ART")) { return 2.0; }
        if(timezone.equals("AST")) { return -9.0; }
        if(timezone.equals("BET")) { return -3.0; }
        if(timezone.equals("BST")) { return 6.0; }
        if(timezone.equals("CAT")) { return -1.0; }
        if(timezone.equals("CNT")) { return -3.5; }
        if(timezone.equals("CST")) { return -6.0; }
        if(timezone.equals("CTT")) { return 8.0; }
        if(timezone.equals("EAT")) { return 3.0; }
        if(timezone.equals("ECT")) { return 1.0; }
        if(timezone.equals("EET")) { return 2.0; }
        if(timezone.equals("EST")) { return -5.0; }
        if(timezone.equals("GMT")) { return 0.0; }
        if(timezone.equals("HST")) { return -10.0; }
        if(timezone.equals("IET")) { return -5.0; }
        if(timezone.equals("IST")) { return 5.5; }
        if(timezone.equals("JST")) { return 9.0; }
        if(timezone.equals("MET")) { return 3.0; }
        if(timezone.equals("MIT")) { return -11.0; }
        if(timezone.equals("MST")) { return -7.0; }
        if(timezone.equals("NET")) { return 4.0; }
        if(timezone.equals("NST")) { return 12.0; }
        if(timezone.equals("PLT")) { return 5.0; }
        if(timezone.equals("PNT")) { return -7.0; }
        if(timezone.equals("PRT")) { return -4.0; }
        if(timezone.equals("PST")) { return -8.0; }
        if(timezone.equals("SST")) { return 11.0; }
        if(timezone.equals("UTC")) { return 0.0; }
        if(timezone.equals("VST")) { return 7.0; }
        return 0.0;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbarmenu, menu);
        return true;
    }

    public void onLogout(MenuItem item) {
        ParseUser.logOut();
        ParseUser currentUser = ParseUser.getCurrentUser(); // this will now be null
        Intent intent = new Intent(CalendarActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void onHome(MenuItem item) {
        Intent intent = new Intent(CalendarActivity.this, HomeActivity.class);
        startActivity(intent);
        setVisible(false);
        //finish();
    }
    public void onSettings(MenuItem item) {
        Intent intent = new Intent(CalendarActivity.this, SettingsActivity.class);
        startActivity(intent);
        setVisible(false);
        //finish();
    }
}