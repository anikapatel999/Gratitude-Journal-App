package com.example.gratitudejournal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.net.SocketOption;
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
        // set the layout manager on the recycler view
        // rvPosts.setLayoutManager(new LinearLayoutManager(this));
        // query posts from Parstagram
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
                Log.i(TAG, "CLICKED!!");
                ParseUser currentUser = ParseUser.getCurrentUser();
                ParseQuery<Entry> query = ParseQuery.getQuery(Entry.class);
                query.setLimit(20);
                query.whereMatches("user", currentUser.getObjectId());
//                Date fd2 = new Date(firstDate.getTime() + (1 * 24 * 60 * 60 * 1000));
//                query.whereGreaterThan("createdAt", fd2);
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
                        // for debugging purposes let's print every post description to logcat
                        for (Entry entry : entries) {
                            Log.i(TAG, "qquery " + entry.getCreatedAt());
                        }

                        allEntries.clear();
                        adapter.notifyDataSetChanged();
                        // save received posts to list and notify adapter of new data
                        firstDate = entries.get(entries.size()-1).getCreatedAt();
                        queryPosts(firstDate, entries.size());
                    }
                });
            }
        });
    }



    private void loadNextDataFromApi(int page) {
        // Send an API request to retrieve appropriate paginated data
        //  --> Send the request including an offset value (i.e `page`) as a query parameter.
        //  --> Deserialize and construct new model objects from the API response
        //  --> Append the new data objects to the existing set of items inside the array of items
        //  --> Notify the adapter of the new items made with `notifyItemRangeInserted()`

        //rvEntries.getLayoutManager().findFirstVisibleItemPosition();

        ParseUser currentUser = ParseUser.getCurrentUser();
        ParseQuery<Entry> query = ParseQuery.getQuery(Entry.class);

        query.setLimit(20);
        query.whereMatches("user", currentUser.getObjectId());
//        query.whereGreaterThan("createdAt", lastDate);
//        query.addAscendingOrder("createdAt");

        query.whereLessThan("createdAt", lastDate);
        query.addDescendingOrder("createdAt");

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
                //firstDate = entries.get(0).getCreatedAt();
                lastDate = entries.get(entries.size()-1).getCreatedAt();
            }
        });
    }

    private void queryPosts(Date d, int pos) {
        // specify what type of data we want to query - Post.class
        ParseUser currentUser = ParseUser.getCurrentUser();
        ParseQuery<Entry> query = ParseQuery.getQuery(Entry.class);

        query.whereMatches("user", currentUser.getObjectId());

        query.whereLessThanOrEqualTo("createdAt", d); //was d2
        query.addDescendingOrder("createdAt"); //CAN CHANGE TO query.addDescendingOrder("createdAt");

        query.setLimit(20); // 15 or 20

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
                    Log.i(TAG, "Entry: " + "original query entry" + entry.getCreatedAt());
                }
                // save received posts to list and notify adapter of new data
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