package com.example.gratitudejournal.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gratitudejournal.EndlessRecyclerViewScrollListener;
import com.example.gratitudejournal.EntriesAdapter;
import com.example.gratitudejournal.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.example.gratitudejournal.parse.Entry;

public class ScrollActivity extends AppCompatActivity {

    public static final String TAG = "ScrollActivity";
    protected EntriesAdapter adapter;
    protected List<Entry> allEntries;
    private RecyclerView rvEntries;
    private TextView tvLoadMore;
    private EndlessRecyclerViewScrollListener scrollListener;
    public int scrollind = 0;
    public Date lastDate;
    public Date firstDate;
    static int y;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.new_color)));

        LinearLayout ll = findViewById(R.id.ll);
        ll.setBackgroundResource(R.color.warm);

        rvEntries = (RecyclerView) findViewById(R.id.rvEntries);
        tvLoadMore = findViewById(R.id.tvLoadMore);

        allEntries = new ArrayList<>();
        adapter = new EntriesAdapter(this, allEntries);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvEntries.setLayoutManager(linearLayoutManager);

        // set the adapter on the recycler view
        rvEntries.setAdapter(adapter);

        Bundle extras = getIntent().getExtras();
        int year = Integer.parseInt(extras.getString("year"));
        int month = Integer.parseInt(extras.getString("month")) - 1;
        int dayOfMonth = Integer.parseInt(extras.getString("dayOfMonth"));
        int index = extras.getInt("index");
        Date d1 = new Date(year-1900, month, dayOfMonth);
        Date d2 = new Date(d1.getTime() + (1 * 24 * 60 * 60 * 1000));
        queryPosts(d2, 0);

        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {

            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {

                Log.i(TAG, "scroll registered, page: " + page);
                loadNextDataFromApi(page);
            }
        };

        // Adds the scroll listener to RecyclerView
        rvEntries.addOnScrollListener(scrollListener);

        tvLoadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "clicked");
                ParseUser currentUser = ParseUser.getCurrentUser();
                ParseQuery<Entry> query = ParseQuery.getQuery(Entry.class);
                query.setLimit(20);
                query.whereMatches("user", currentUser.getObjectId());

                query.whereGreaterThan("createdAt", firstDate);
                query.addAscendingOrder("createdAt");
                query.findInBackground(new FindCallback<Entry>() {
                    @Override
                    public void done(List<Entry> entries, ParseException e) {
                        // check for errors
                        if (e != null) {
                            Log.e(TAG, "Issue with getting posts", e);
                            return;
                        }
                        if (entries.size() == 0) {
                            Toast.makeText(ScrollActivity.this, "No more entries", Toast.LENGTH_LONG).show();
                            return;
                        }
                        // for debugging purposes let's print every entry description to logcat
                        for (Entry entry : entries) {
                            Log.d(TAG, "date created of entry: " + entry.getCreatedAt());
                        }

                        allEntries.clear();
                        adapter.notifyDataSetChanged();
                        // save received entries to list and notify adapter of new data
                        firstDate = entries.get(entries.size()-1).getCreatedAt();
                        queryPosts(firstDate, entries.size());
                    }
                });
            }
        });
    }



    private void loadNextDataFromApi(int page) {

        ParseUser currentUser = ParseUser.getCurrentUser();
        ParseQuery<Entry> query = ParseQuery.getQuery(Entry.class);

        query.setLimit(20);
        query.whereMatches("user", currentUser.getObjectId());

        query.whereLessThan("createdAt", lastDate);
        query.addDescendingOrder("createdAt");

        // start an asynchronous call for entries
        query.findInBackground(new FindCallback<Entry>() {
            @Override
            public void done(List<Entry> entries, ParseException e) {
                // check for errors
                if (e != null) {
                    Log.e(TAG, "Issue with getting posts", e);
                    return;
                }
                if (entries.size() == 0) {
                    return;
                }
                // for debugging purposes let's print every entry description to logcat
                for (Entry entry : entries) {
                    Log.d(TAG, "infinite scroll entry");
                }

                // save received entry to list and notify adapter of new data
                allEntries.addAll(entries);
                adapter.notifyItemRangeInserted(scrollind, entries.size());
                scrollind = scrollind + entries.size() - 1;
                lastDate = entries.get(entries.size()-1).getCreatedAt();
            }
        });
    }

    private void queryPosts(Date d, int pos) {
        ParseUser currentUser = ParseUser.getCurrentUser();
        ParseQuery<Entry> query = ParseQuery.getQuery(Entry.class);

        query.whereMatches("user", currentUser.getObjectId());

        query.whereLessThanOrEqualTo("createdAt", d);
        query.addDescendingOrder("createdAt");

        query.setLimit(20);

        Log.d(TAG, "query: " + query);

        // start an asynchronous call for entries
        query.findInBackground(new FindCallback<Entry>() {
            @Override
            public void done(List<Entry> entries, ParseException e) {
                // check for errors
                if (e != null) {
                    Log.e(TAG, "Issue with getting posts", e);
                    return;
                }

                for (Entry entry : entries) {
                    Log.i(TAG, "original query entry: " + entry.getCreatedAt());
                }
                // save received entries to list and notify adapter of new data
                lastDate = entries.get(entries.size()-1).getCreatedAt();
                firstDate = entries.get(0).getCreatedAt();
                scrollind = entries.size()-1;
                allEntries.addAll(entries);
                adapter.notifyDataSetChanged();
                rvEntries.smoothScrollToPosition(pos);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbarmenu, menu);
        menu.findItem(R.id.mentions).setVisible(false);
        return true;
    }

    public void onMentions(MenuItem item) {
        Intent intent = new Intent(ScrollActivity.this, ViewMentionsActivity.class);
        startActivity(intent);
    }

    public void onLogout(MenuItem item) {
        ParseUser.logOut();
        ParseUser currentUser = ParseUser.getCurrentUser();
        Intent intent = new Intent(ScrollActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void onHome(MenuItem item) {
        Intent intent = new Intent(ScrollActivity.this, HomeActivity.class);
        startActivity(intent);
    }

    public void onSettings(MenuItem item) {
        Intent intent = new Intent(ScrollActivity.this, SettingsActivity.class);
        startActivity(intent);
    }

}