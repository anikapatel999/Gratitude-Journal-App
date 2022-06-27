package com.example.gratitudejournal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.example.myapplication.Entry;
import com.example.myapplication.User;

public class ScrollActivity extends AppCompatActivity {

    public static final String TAG = "ScrollActivity";
    protected EntriesAdapter adapter;
    protected List<Entry> allEntries;
    private RecyclerView rvEntries;
    private EndlessRecyclerViewScrollListener scrollListener;
    public int scrollind = 0;
    public Date lastDate;

    //TODO:
    // 1. query posts by user, using the greater than / less than in the CalendarActivity
    //    method to make sure they're (for example)
    //    10 entries before and 10 entries after the desired date
    // 2. Also make sure that (!mood.equalTo("skip) || !text.equalTo("No entry"))
    //    so that we don't display any fully empty entries

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll);

        rvEntries = (RecyclerView) findViewById(R.id.rvEntries);

        allEntries = new ArrayList<>();
        adapter = new EntriesAdapter(this, allEntries);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvEntries.setLayoutManager(linearLayoutManager);

        // set the adapter on the recycler view
        rvEntries.setAdapter(adapter);
        // set the layout manager on the recycler view
        // rvPosts.setLayoutManager(new LinearLayoutManager(this));
        // query posts from Parstagram
        queryPosts();

        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                Log.i(TAG, "scroll registered");
                loadNextDataFromApi(page);
            }
        };

        // Adds the scroll listener to RecyclerView
        rvEntries.addOnScrollListener(scrollListener);
    }

    private void loadNextDataFromApi(int page) {
        // Send an API request to retrieve appropriate paginated data
        //  --> Send the request including an offset value (i.e `page`) as a query parameter.
        //  --> Deserialize and construct new model objects from the API response
        //  --> Append the new data objects to the existing set of items inside the array of items
        //  --> Notify the adapter of the new items made with `notifyItemRangeInserted()`

        ParseUser currentUser = ParseUser.getCurrentUser();
        ParseQuery<Entry> query = ParseQuery.getQuery(Entry.class);

        query.setLimit(20);
        query.whereMatches("user", currentUser.getObjectId());
        query.whereGreaterThan("createdAt", lastDate);
        query.addAscendingOrder("createdAt");

        // TODO: make the query with the right criteria

        // start an asynchronous call for posts
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
                // for debugging purposes let's print every post description to logcat
                for (Entry entry : entries) {
                    Log.i(TAG, "infinite scroll entry");
                }

                // save received posts to list and notify adapter of new data
                allEntries.addAll(entries);
                //scrollind = scrollind + query.getLimit();
                adapter.notifyItemRangeInserted(scrollind, entries.size());
                scrollind = scrollind + entries.size() - 1;
                // swipeContainer.setRefreshing(false);
                lastDate = entries.get(entries.size()-1).getCreatedAt();
            }
        });
    }

    private void queryPosts() {
        // specify what type of data we want to query - Post.class
        ParseUser currentUser = ParseUser.getCurrentUser();
        ParseQuery<Entry> query = ParseQuery.getQuery(Entry.class);

        query.whereMatches("user", currentUser.getObjectId());

        Bundle extras = getIntent().getExtras();
        int year = Integer.parseInt(extras.getString("year"));
        int month = Integer.parseInt(extras.getString("month")) - 1;
        int dayOfMonth = Integer.parseInt(extras.getString("dayOfMonth"));
        Date d1 = new Date(year-1900, month, dayOfMonth);
        query.whereGreaterThanOrEqualTo("createdAt", d1);

        query.addAscendingOrder("createdAt"); //CAN CHANGE TO query.addDescendingOrder("createdAt");

        query.setLimit(8); // 15 or 20

        Log.i(TAG, "queryYY: " + query);

        // start an asynchronous call for posts
        query.findInBackground(new FindCallback<Entry>() {
            @Override
            public void done(List<Entry> entries, ParseException e) {
                // check for errors
                if (e != null) {
                    Log.e(TAG, "Issue with getting posts", e);
                    return;
                }

                // for debugging purposes let's print every post description to logcat
                for (Entry entry : entries) {
                    Log.i(TAG, "Entry: " + "original query entry" + entry);
                }

                // save received posts to list and notify adapter of new data
                lastDate = entries.get(entries.size()-1).getCreatedAt();
                scrollind = entries.size()-1;
                allEntries.addAll(entries);
                adapter.notifyDataSetChanged();
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
        Intent intent = new Intent(ScrollActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void onHome(MenuItem item) {
        Intent intent = new Intent(ScrollActivity.this, HomeActivity.class);
        startActivity(intent);
        // finish();
    }

    public void onSettings(MenuItem item) {
        Intent intent = new Intent(ScrollActivity.this, SettingsActivity.class);
        startActivity(intent);
        //finish();
    }

}