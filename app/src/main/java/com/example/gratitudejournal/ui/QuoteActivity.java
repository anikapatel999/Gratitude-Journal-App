package com.example.gratitudejournal.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.gratitudejournal.Globals;
import com.example.gratitudejournal.R;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.example.gratitudejournal.parse.User;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.example.gratitudejournal.parse.Entry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class QuoteActivity extends AppCompatActivity {

    public static final String TAG = "QuoteActivity";
    public static final String quotesAPI = "https://zenquotes.io/api/quotes";
    public static final String dictAPI = "https://api.dictionaryapi.dev/api/v2/entries/en/";
    float x1, x2, y1, y2;
    protected List<Entry> allEntries;
    String[] selectedKeywords = {};
    String[] keywordArray = {"inspiration", "excellence", "happiness", "dreams", "courage",
            "confidence", "kindness", "success", "change", "future", "life", "living",
            "today", "choice", "freedom"};
    String[] searchWords = {};
    ArrayList<String> synonyms = new ArrayList<>();
    ArrayList<String> roots = new ArrayList<>();
    ArrayList<String> quotes = new ArrayList<>();
    ArrayList<String> authors = new ArrayList<>();
    private TextView tvQuote;
    private TextView tvAuthor;
    private TextView tvCredit;
    Animation fade_in_anim;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quote);
        tvQuote = findViewById(R.id.tvQuote);
        tvAuthor = findViewById(R.id.tvAuthor);
        tvCredit = findViewById(R.id.tvCredit);
        fade_in_anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.new_color)));

        ConstraintLayout cl = findViewById(R.id.cl);
        cl.setBackgroundResource(R.color.warm);

        Log.i(TAG, "oncreateeeeeee");
        ParseUser currentUser = ParseUser.getCurrentUser();
        User currentUser2 = (User) currentUser;
        if (currentUser2.getCurrentEntry().getText().equals(Globals.no_entry)) {
            Intent intent = new Intent(QuoteActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }

        // Query the last 7 entries by the user
        ParseQuery<Entry> query = ParseQuery.getQuery(Entry.class);
        query.setLimit(7);
        query.whereMatches("user", currentUser.getObjectId());
        query.addDescendingOrder("createdAt");
        Log.i(TAG, "query " + query);


        try {
            Log.i(TAG, "find " + query);
            allEntries = query.find();
            Log.i(TAG, "allEntries " + allEntries);
            Log.i(TAG, "to make sure individual elements can be accessed: " + allEntries.get(0).getText());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // check that the user does have 7 entries
        Log.i(TAG, "all entries: " + allEntries);
        modifySelectedKeywords();

        String entryText = allEntries.get(0).getText();
        searchWords = split(entryText);

        // sort searchWords by length
        Arrays.sort(searchWords, Comparator.comparingInt(String::length));
        searchWords = reverse(searchWords);
        searchWords = slicer(searchWords);
        getSynonyms(searchWords);
        Log.i(TAG, ":))))))) " + synonyms);
        Log.i(TAG, "dlkfmvlfkdmbv" + roots);

        tvQuote.setText(R.string.thank_you_for_writing_this_entry);
    }

    private void modifySelectedKeywords() {
        Boolean sevenMoodsSelected = true;
        double moodScore = 0;

        if (allEntries.size() == 7) {
            // check if the user skipped any of the last 7 moods
            for (int i = 0; i < allEntries.size(); i++) {
                if (allEntries.get(i).getMood().equals(Globals.skip) || allEntries.get(i).getMood().equals("No mood selected")) {
                    Log.i(TAG, "calc" + allEntries.get(i).getMood() + " " + allEntries.get(i).getCreatedAt());
                    sevenMoodsSelected = false;
                }
            }
            // if the user selected all of the 7 past moods
            if (sevenMoodsSelected) {
                moodScore = calcCurrentMoodScore();
                moodScore = calcTotalMoodScore(moodScore);
                selectedKeywords = keywordsFromTotalScore(moodScore);
                Log.i(TAG, "kw1 " + Arrays.toString(selectedKeywords));
            }
            // if the user did not select all of the 7 past moods but did select the current mood
            else if (!(allEntries.get(0).getMood().equals(Globals.skip)) && !(allEntries.get(0).getMood().equals("No mood selected"))) {
                moodScore = calcCurrentMoodScore();
                selectedKeywords = keywordsFromCurrentScore(moodScore);
                Log.i(TAG, "kw2 " + Arrays.toString(selectedKeywords));
            }
            // if the user did neither
            else {
                selectedKeywords = Arrays.copyOfRange(keywordArray, 4, 10);
                Log.i(TAG, "kw3 " + Arrays.toString(selectedKeywords));
            }
        }
        // if the user does not have 7 past entries
        else {
            selectedKeywords = Arrays.copyOfRange(keywordArray, 4, 10);
            Log.i(TAG, "kw4 " + Arrays.toString(selectedKeywords));
        }
    }

    private void getSynonyms(String[] sw) {

        String tempDictAPI = "";
        for (int i = 0; i < sw.length; i++) {
            synonyms.add(sw[i]);
            tempDictAPI = dictAPI + sw[i];
            AsyncHttpClient client = new AsyncHttpClient();
            int finalI = i;
            int finalI1 = i;
            client.get(tempDictAPI, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Headers headers, JSON json) {
                    Log.i(TAG, sw[finalI] + " onSuccess");
                    Log.i(TAG, json.toString());

                    JSONArray jsonArray = json.jsonArray;
                    try {
                        JSONObject j = (JSONObject) jsonArray.get(0);
                        JSONArray j2 = j.getJSONArray("meanings");
                        for (int ind = 0; ind < j2.length(); ind++) {
                            JSONObject j3 = (JSONObject) j2.get(ind);
                            Object j4 = j3.get("synonyms");
                            JSONArray j5 = (JSONArray) j4;
                            for (int index = 0; index < j5.length(); index++) {
                                Log.i(TAG, ":) " + j5.get(index));
                                synonyms.add(j5.get(index).toString());
                            }
                            Log.i(TAG, "synonyms: " + j.getString("word") + " " + j4);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.i(TAG, "FINAL TO COMPARE: " + String.valueOf(finalI1) + " " + String.valueOf(sw.length - 1));
                    if (finalI1 == sw.length - 1) {
                        for (int a = 0; a < 5; a++) {
                            synonyms.add(0, selectedKeywords[a]);
                        }
                        Log.i(TAG, ":)( " + synonyms);
                        rootFinder();
                    }
                }

                @Override
                public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                    Log.i(TAG, sw[finalI] + " onFailure");
                    if (finalI1 == sw.length - 1) {
                        for (int a = 0; a < 5; a++) {
                            synonyms.add(0, selectedKeywords[a]);
                        }
                        Log.i(TAG, ":)( " + synonyms);
                        rootFinder();
                    }
                }
            });
            Log.i(TAG, "LIST OF SYNONYMS1 :) " + synonyms);
        }
        Log.i(TAG, "LIST OF SYNONYMS2 :) " + synonyms);
        //return syns;
    }

    private void rootFinder() {

        String str = synonyms.get(0);
        Log.i(TAG, ":( " + synonyms.size());
        for (int i = 0; i < synonyms.size(); i++) {
            str = synonyms.get(i);

            Log.i(TAG, "FIRST: " + str.length());

            if (str.length() > 4) {
                if (str.substring(str.length() - 4).equals("ship") || str.substring(str.length() - 4).equals("ness")) {
                    str = str.substring(0, str.length() - 4);
                    synonyms.set(i, str);
                }
            }

            if (str.length() > 2) {
                if (str.substring(str.length() - 2).equals("es") || str.substring(str.length() - 2).equals("ed")) {
                    str = str.substring(0, str.length() - 2);
                    synonyms.set(i, str);
                    Log.i(TAG, "executed: " + str + " " + synonyms.get(i));
                }
            }
            if (str.length() > 1) {
                if (str.substring(str.length() - 1).equals("s")) {
                    Log.i(TAG, "dlkfmvlfkdmbv");
                    str = str.substring(0, str.length() - 1);
                    synonyms.set(i, str);
                }
            }
            if (str.length() > 3) {
                if (str.substring(str.length() - 3).equals("ing") || str.substring(str.length() - 3).equals("ful") || str.substring(str.length() - 3).equals("est")) {
                    str = str.substring(0, str.length() - 3);
                    synonyms.set(i, str);
                }
            }

            if (str.length() > 5) {
                if (str.substring(str.length() - 5).equals("ation")) {
                    str = str.substring(0, str.length() - 5);
                    synonyms.set(i, str);
                }
            }
        }
        Log.i(TAG, "THESE ARE THE SYNONYMS" + synonyms);
        roots.addAll(synonyms);
        Log.i(TAG, "dlkfmvlfkdmbvaaa" + roots);

        //TODO: CHANGE BACK TO GETQUOTES?
        getQuotes();
    }

    private void getQuotes() {
        for (int count = 0; count < 5; count++) {
            AsyncHttpClient client = new AsyncHttpClient();
            int finalCount = count;
            client.get(quotesAPI, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Headers headers, JSON json) {
                    Log.i(TAG, "onSuccess Quotes");
                    JSONArray jsonArray = json.jsonArray;
                    try {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject j = (JSONObject) jsonArray.get(i);
                            String quote = j.getString("q");
                            String author = j.getString("a");
                            quotes.add(quote);
                            authors.add(author);
                            Log.i(TAG, "FOR DEBUGGING " + quotes.size() + " " + authors.size());
                            if (finalCount == 4 && (i == jsonArray.length() - 1)) {
                                Log.i(TAG, "Yippee" + quotes.size());
                                searchQuotes();
                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                    Log.i(TAG, "onFailure Quotes");
                }
            });
        }

    }

    private void getQuotes2() {
//        for (int count = 0; count < 5; count++){
//            int finalCount = count;
        int cacheSize = 10 * 1024 * 1024;

        Cache cache = new Cache(new File(getApplication().getCacheDir(), "cacheFileName"), cacheSize);

        OkHttpClient client = new OkHttpClient.Builder().cache(cache).build();

        // OkHttpClient client = new OkHttpClient();
        //Request request = new Request.Builder().url(quotesAPI).build();
        Request request = new Request.Builder()
                .cacheControl(new CacheControl.Builder()
                        .maxAge(7, TimeUnit.DAYS)
                        .build())
                .url(quotesAPI)
                .build();
        for (int count = 0; count < 5; count++) {
            int finalCount = count;
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    Log.i(TAG, "onFailure Quotes");
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    Log.i(TAG, "onSuccess Quotes");
                    JSONArray jsonArray = null;
                    try {
                        Log.i(TAG, "ldkmvdlkfmv: " + response.cacheResponse());
                        jsonArray = new JSONArray(response.body().string()); // this line should make it get added to the cache
                        Log.i(TAG, "for debugging");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    try {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject j = (JSONObject) jsonArray.get(i);
                            String quote = j.getString("q");
                            String author = j.getString("a");
                            quotes.add(quote);
                            authors.add(author);
                            Log.i(TAG, "FOR DEBUGGING " + quotes.size() + " " + authors.size() + " " + quotes.get(quotes.size() - 1));
                            if (finalCount == 4 && (i == jsonArray.length() - 1)) {
                                Log.i(TAG, "Yippee" + quotes.size() + " " + quotes.get(quotes.size() - 1));
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        searchQuotes();
                                    }
                                });
                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });
        }
    }

    private void searchQuotes() {
        boolean set = false;
        for (int i = 0; i < quotes.size(); i++) {
            if (set) {
                break;
            }
            for (int j = 0; j < roots.size(); j++) {
                if (quotes.get(i).equals("Too many requests. Obtain an auth key for unlimited access.")) {
                    tvQuote.startAnimation(fade_in_anim);
                    tvQuote.setText(R.string.thank_you);
                    set = true;
                    break;
                }
                if (quotes.get(i).contains(roots.get(j))) {
                    boolean passesFilter = filter(quotes.get(i));
                    if (passesFilter) {
                        tvQuote.startAnimation(fade_in_anim);
                        tvQuote.setText(quotes.get(i));
                        tvAuthor.startAnimation(fade_in_anim);
                        tvAuthor.setText("- " + authors.get(i));
                        tvCredit.startAnimation(fade_in_anim);
                        tvCredit.setVisibility(View.VISIBLE);
                        set = true;
                        Log.i(TAG, "SET THE QUOTE FROM WORD SEARCH " + roots.get(j) + " " + quotes.get(i));
                        break;
                    }
                }
            }
        }
        if (!set) {
            int ind = (int) Math.floor(Math.random() * (249 - 0 + 1) - .000001);
            tvQuote.startAnimation(fade_in_anim);
            tvQuote.setText(quotes.get(ind));
            tvAuthor.startAnimation(fade_in_anim);
            tvAuthor.setText("- " + authors.get(ind));
            Log.i(TAG, "SET THE QUOTE FROM RANDOM");
        }
    }

    private boolean filter(String s) {
        boolean safe = true;
        String[] filterWords = {"death", "die", "dying", "loss", "lose", "lie", "gone", "youth", "young", "old", "age", "worse", "worst", "god", "pig"};
        for (int i = 0; i < filterWords.length; i++) {
            if (s.contains(filterWords[i])) {
                safe = false;
                break;
            }
        }
        return safe;
    }

    private String[] slicer(String[] words) {
        if (words.length > 15) {
            words = Arrays.copyOfRange(searchWords, 0, 15);
        }
        return words;
    }

    private void forTesting(String[] arr, String tag) {
        for (int i = 0; i < arr.length; i++) {
            System.out.println(tag + ": " + arr[i]);
        }
    }

    private String[] reverse(String[] rev) {
        for (int i = 0; i < rev.length / 2; i++) {
            String temp = rev[i];
            rev[i] = rev[rev.length - i - 1];
            rev[rev.length - i - 1] = temp;
        }
        return rev;
    }

    private String[] split(String entryText) {
        String[] s = entryText.split("\\s+");
        for (int i = 0; i < s.length; i++) {
            s[i] = s[i].replaceAll("[^\\w]", "");
            Log.i(TAG, "THIS IS A WORD IN THE SPLIT " + s[i]);
        }
        Log.i(TAG, "THIS IS THE SPLIT STRING" + s);
        return s;
    }

    private String[] keywordsFromCurrentScore(double moodScore) {

        String[] skw = {};

        if (moodScore > 15) {
            skw = Arrays.copyOfRange(keywordArray, 0, 5);
        } else if (moodScore <= 15 && moodScore >= -15) {
            skw = Arrays.copyOfRange(keywordArray, 4, 10);
        } else if (moodScore < -15) {
            skw = Arrays.copyOfRange(keywordArray, 9, 15);
        }

        Log.i(TAG, "keywordsFromCurrentScore: " + skw);
        return skw;
    }

    private String[] keywordsFromTotalScore(double moodScore) {

        String[] skw = {};

        if (moodScore > 30) {
            skw = Arrays.copyOfRange(keywordArray, 0, 5);
        } else if (moodScore <= 30 && moodScore >= -30) {
            skw = Arrays.copyOfRange(keywordArray, 4, 10);
        } else if (moodScore < -30) {
            skw = Arrays.copyOfRange(keywordArray, 9, 15);
        }

        Log.i(TAG, "keywordsFromTotalScore: " + skw);
        return skw;
    }

    private double calcTotalMoodScore(double moodScore) {
        //ADD AND SUBTRACT BASED ON THE MOODS OF THE PREVIOUS 6 DAYS
        for (int i = 1; i < allEntries.size(); i++) {
            if (allEntries.get(i).getMood().equals(Globals.terrible)) {
                moodScore = moodScore - 3;
            } else if (allEntries.get(i).getMood().equals(Globals.bad)) {
                moodScore = moodScore - 1.5;
            } else if (allEntries.get(i).getMood().equals(Globals.okay)) {
                moodScore = moodScore + 0;
            } else if (allEntries.get(i).getMood().equals(Globals.good)) {
                moodScore = moodScore + 1.5;
            } else if (allEntries.get(i).getMood().equals(Globals.amazing)) {
                moodScore = moodScore + 3;
            }
        }
        Log.i(TAG, "calcTotalMoodScore: " + moodScore);
        return moodScore;
    }

    private double calcCurrentMoodScore() {
        // calculates the initial mood score based on
        // the mood the user selected for the current entry

        double moodScore = 0;
        String currentMood = allEntries.get(0).getMood();

        if (currentMood.equals(Globals.terrible)) {
            moodScore = -30;
        } else if (currentMood.equals(Globals.bad)) {
            moodScore = -15;
        } else if (currentMood.equals(Globals.okay)) {
            moodScore = 0;
        } else if (currentMood.equals(Globals.good)) {
            moodScore = 15;
        } else if (currentMood.equals(Globals.amazing)) {
            moodScore = 30;
        }
        Log.i(TAG, "calcCurrentMoodScore: " + moodScore);
        return moodScore;
    }

    public boolean onTouchEvent(MotionEvent touchEvent) {
        switch (touchEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x1 = touchEvent.getX();
                y1 = touchEvent.getY();
                break;
            case MotionEvent.ACTION_UP:
                x2 = touchEvent.getX();
                y2 = touchEvent.getY();

                if (x1 > x2) {
                    Intent i = new Intent(QuoteActivity.this, HomeActivity.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    Log.i("swiped left", "it worked");
                }
                break;
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbarmenu, menu);
        menu.findItem(R.id.mentions).setVisible(false);
        return true;
    }

    public void onMentions(MenuItem item) {
        Intent intent = new Intent(QuoteActivity.this, ViewMentionsActivity.class);
        startActivity(intent);
    }

    public void onLogout(MenuItem item) {
        ParseUser.logOut();
        ParseUser currentUser = ParseUser.getCurrentUser(); // this will now be null
        Intent intent = new Intent(QuoteActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void onHome(MenuItem item) {
        Intent intent = new Intent(QuoteActivity.this, HomeActivity.class);
        startActivity(intent);

    }

    public void onSettings(MenuItem item) {
        Intent intent = new Intent(QuoteActivity.this, SettingsActivity.class);
        startActivity(intent);
    }
}