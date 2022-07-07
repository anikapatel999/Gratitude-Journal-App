package com.example.gratitudejournal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.example.myapplication.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import com.example.myapplication.Entry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Headers;

public class QuoteActivity extends AppCompatActivity {

    public static final String TAG = "QuoteActivity";
    public static final String quotesAPI = "https://zenquotes.io/api/quotes"; //TODO: CHANGE THIS LATER TO HAVE KEYWORDS (will have to use string concatenation)
    public static final String dictAPI = "https://api.dictionaryapi.dev/api/v2/entries/en/";
    float x1, x2, y1, y2;
    protected List<com.example.myapplication.Entry> allEntries;
    String[] selectedKeywords = {};
    String[] keywordArray = {"inspiration", "excellence", "happiness", "dreams", "courage",
            "confidence", "kindness", "success", "change", "future", "life", "living",
            "today", "choice", "freedom"};
    String[] searchWords = {};
    //String[] synonyms = {};
    ArrayList<String> synonyms = new ArrayList<>();
    ArrayList<String> roots = new ArrayList<>();
    ArrayList<String> quotes = new ArrayList<>();
    ArrayList<String> authors = new ArrayList<>();
    private TextView tvQuote;
    private TextView tvAuthor;
    Animation fade_in_anim;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quote);
        tvQuote = findViewById(R.id.tvQuote);
        tvAuthor = findViewById(R.id.tvAuthor);
        fade_in_anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.new_color)));

        Log.i(TAG, "oncreateeeeeee");
        ParseUser currentUser = ParseUser.getCurrentUser();
        User currentUser2 = (User) currentUser;
        if (currentUser2.getCurrentEntry().getText().equals("No entry")) {
            Intent intent = new Intent(QuoteActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }
//
//        ArrayList<String> keywordArray = new ArrayList<String>();
//        keywordArray.add("Inspiration");

        // List of keywords that will be used

        // TODO: select a sub array of keywordArray based on the score calculated from the user's
        //  past mood selections

        // Query the last 7 entries by the user
        ParseQuery<Entry> query = ParseQuery.getQuery(Entry.class);
        query.setLimit(7);
        query.whereMatches("user", currentUser.getObjectId());
        query.addDescendingOrder("createdAt");
        Log.i(TAG, "query " + query);

        // query.findInBackground() DOESN'T WORK,
        // probably because asynchronous so it's null when the rest of the code executes
        try {
            Log.i(TAG, "find " + query);
            allEntries = query.find();
            Log.i(TAG, "allEntries " + allEntries);
            Log.i(TAG, "to make sure individual elements can be accessed: " + allEntries.get(0).getText());
        } catch (ParseException e) {
            e.printStackTrace();
        }

//        query.findInBackground(new FindCallback<Entry>() {
//            @Override
//            public void done(List<Entry> entries, ParseException e) {
//                Log.i(TAG, "findinbackground " + query);
//                // check for errors
//                if (e != null) {
//                    Log.e(TAG, "Issue with getting entries", e);
//                    return;
//                }
//                if (entries.size() == 0) {
//                    return;
//                }
//                // for debugging purposes let's print every post description to logcat
//                for (Entry entry : entries) {
//                    Log.i(TAG, "entry from the query: " + entry.getText());
//                }
//                Log.i(TAG, "got to this point");
//                allEntries.addAll(entries);
//            }
//        });

        //CALCULATE THE MOOD SCORE
        Boolean sevenMoodsSelected = true;
        double moodScore = 0;

        // check that the user does have 7 entries
        Log.i(TAG, "all entries: " + allEntries);
        if (allEntries.size() == 7) {

            // check if the user skipped any of the last 7 moods
            for (int i = 0; i < allEntries.size(); i++){
                if(allEntries.get(i).getMood().equals("skip") || allEntries.get(i).getMood().equals("No mood selected")) {
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
            else if (!(allEntries.get(0).getMood().equals("skip")) && !(allEntries.get(0).getMood().equals("No mood selected"))) {
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


        //TODO: Once you get the API key, you can do this once for each keyword.
        // For now, I'm just doing the same random call 5 times. If you can't get the API key,
        // use the keywords you selected the same way as you're planning to use the words
        // from the user's previous entry (so like append those 5 words to the array of the
        // longest few words from the entry, cut off the endings, and search through all the
        // quotes for that root, and return it if found).

        String entryText = allEntries.get(0).getText();
        //entryText = "I would love to have a dog, dogs are very fun, yes :)";
        searchWords = split(entryText);
        // forTesting(searchWords, "sw1");
        // sort searchWords by length
        Arrays.sort(searchWords, Comparator.comparingInt(String::length));
        // forTesting(searchWords, "sw2");
        searchWords = reverse(searchWords);
        // forTesting(searchWords, "sw3");
        searchWords = slicer(searchWords);
//        synonyms = getSynonyms(searchWords);
        getSynonyms(searchWords);
        Log.i(TAG, ":))))))) " + synonyms);
        Log.i(TAG, "dlkfmvlfkdmbv" + roots);
        // rootFinder(synonyms);

        //TODO: CHANGE THIS LATER
        tvQuote.setText("Thank you for writing this entry");
//        tvAuthor.setText("-Author's Name");

    }

    private void getSynonyms(String[] sw) {
//        synonyms = new ArrayList<>();
//        final ArrayList[] syns = {new ArrayList()};
        String tempDictAPI = "";
        for (int i = 0; i< sw.length; i++) {
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
                        //Log.i(TAG, "THIS IS THE WORD: " + j.getString("word"));
                        JSONArray j2 = j.getJSONArray("meanings");
                        for(int ind = 0; ind < j2.length(); ind++) {
                            //Log.i(TAG, "THIS IS THE PART OF SPEECH " + j2.get(0));
                            JSONObject j3 = (JSONObject) j2.get(ind);
                            Object j4 = j3.get("synonyms");
                            JSONArray j5 = (JSONArray) j4;
                            for (int index = 0; index < j5.length(); index++){
                                Log.i(TAG, ":) " + j5.get(index));
                                synonyms.add(j5.get(index).toString());
                                // Log.i(TAG, ":)( " + syns);
                            }
//                            Log.i(TAG, ":)( " + syns);
                            Log.i(TAG, "synonyms: " + j.getString("word") + " " + j4);
                        }
//                        Log.i(TAG, ":)( " + syns);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (finalI1 == sw.length -1) {
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

            if (str.length() > 2 && str.substring(str.length() - 2).equals("es") || str.substring(str.length() - 2).equals("ed")) {
                str = str.substring(0,str.length()-2);
                synonyms.set(i, str);
            }
            else if (str.length() > 1 && str.substring(str.length() - 1).equals("s")) {
                Log.i(TAG, "dlkfmvlfkdmbv");
                str = str.substring(0,str.length()-1);
                synonyms.set(i, str);
            }
            else if (str.length() > 3 && str.substring(str.length() - 3).equals("ing")) {
                str = str.substring(0,str.length()-3);
                synonyms.set(i, str);
            }
            else if (str.length() > 4 && str.substring(str.length() - 4).equals("ship")) {
                str = str.substring(0,str.length()-4);
                synonyms.set(i, str);
            }
            else if (str.length() > 5 && str.substring(str.length() - 5).equals("ation")) {
                str = str.substring(0,str.length()-5);
                synonyms.set(i, str);
            }
        }
        roots.addAll(synonyms);
        Log.i(TAG, "dlkfmvlfkdmbvaaa" + roots);
        getQuotes();
    }

    private void getQuotes() {
        for (int count = 0; count < 5; count++){
            AsyncHttpClient client = new AsyncHttpClient();
            int finalCount = count;
            client.get(quotesAPI, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Headers headers, JSON json) {
                    Log.i(TAG, "onSuccess Quotes");
                    JSONArray jsonArray = json.jsonArray;
                    try {
                        for (int i = 0; i < jsonArray.length(); i++){
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

    private void searchQuotes() {
        boolean set = false;
        for (int i = 0; i < quotes.size(); i++) {
            if(set) {
                break;
            }
            for (int j = 0; j < roots.size(); j++) {
                if (quotes.get(i).equals("Too many requests. Obtain an auth key for unlimited access.")) {
                    tvQuote.startAnimation(fade_in_anim);
                    tvQuote.setText("Thank you for writing this entry");
                    set = true;
                    break;
                }
                if (quotes.get(i).contains(roots.get(j))) {
                    // SHOULD I ADD A LIST OF WORDS THE QUOTE SHOULDNT CONTAIN?
                    // ex: death, die, dying, loss, lose, lie, gone, youth, young, old, age, worst, pig (for that one obama quote lol)
                    tvQuote.startAnimation(fade_in_anim);
                    tvQuote.setText(quotes.get(i));
                    tvAuthor.startAnimation(fade_in_anim);
                    tvAuthor.setText("- " + authors.get(i));
                    set = true;
                    Log.i(TAG, "SET THE QUOTE FROM WORD SEARCH " + roots.get(j) + " " + quotes.get(i));
                    break;
                }
            }
        }
        if (!set) {
            // honestly the quotes are randomized anyway, i could just pick the first one
            int ind = (int) Math.floor(Math.random()*(249-0+1)-.000001);
            tvQuote.startAnimation(fade_in_anim);
            tvQuote.setText(quotes.get(ind));
            tvAuthor.startAnimation(fade_in_anim);
            tvAuthor.setText("- " + authors.get(ind));
            Log.i(TAG, "SET THE QUOTE FROM RANDOM");
        }
    }

    private String[] slicer(String[] words) {
        if (words.length > 15) {
            words = Arrays.copyOfRange(searchWords, 0, 15);
        }
        return words;
    }

    private void forTesting (String[] arr, String tag) {
        for (int i = 0; i< arr.length; i++) {
            System.out.println(tag + ": " + arr[i]);
        }
    }

    private String[] reverse(String[] rev) {
        for(int i = 0; i < rev.length / 2; i++)
        {
            String temp = rev[i];
            rev[i] = rev[rev.length - i - 1];
            rev[rev.length - i - 1] = temp;
        }
        return rev;
    }

    private String[] split(String entryText) {
        // entryText = "I would love to have a dog, dogs are very fun, yes :)";
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
        }
        else if (moodScore <= 15 && moodScore >= -15 ){
            skw = Arrays.copyOfRange(keywordArray, 4, 10);
        }
        else if (moodScore < -15){
            skw = Arrays.copyOfRange(keywordArray, 9, 15);
        }

        Log.i(TAG, "keywordsFromCurrentScore: " + skw);
        return skw;
    }

    private String[] keywordsFromTotalScore(double moodScore) {

        String[] skw = {};

        if (moodScore >= 30) {
            skw = Arrays.copyOfRange(keywordArray, 0, 5);
        }
        else if (moodScore < 30 && moodScore > -30 ){
            skw = Arrays.copyOfRange(keywordArray, 4, 10);
        }
        else if (moodScore <= -30){
            skw = Arrays.copyOfRange(keywordArray, 9, 15);
        }

        Log.i(TAG, "keywordsFromTotalScore: " + skw);
        return skw;
    }

    private double calcTotalMoodScore(double moodScore) {
        //ADD AND SUBTRACT BASED ON THE MOODS OF THE PREVIOUS 6 DAYS
        for (int i = 1; i < allEntries.size(); i++) {
            if (allEntries.get(i).getMood().equals ("Terrible")) {
                moodScore = moodScore - 2;
            }
            else if (allEntries.get(i).getMood().equals ("Bad")) {
                moodScore = moodScore - 1;
            }
            else if (allEntries.get(i).getMood().equals ("Okay")) {
                moodScore = moodScore + 0;
            }
            else if (allEntries.get(i).getMood().equals ("Good")) {
                moodScore = moodScore + 1;
            }
            else if (allEntries.get(i).getMood().equals ("Amazing")) {
                moodScore = moodScore + 2;
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

        if (currentMood.equals ("Terrible")) {
            moodScore = -30;
        }
        else if (currentMood.equals ("Bad")) {
            moodScore = -15;
        }
        else if (currentMood.equals ("Okay")) {
            moodScore = 0;
        }
        else if (currentMood.equals ("Good")) {
            moodScore = 15;
        }
        else if (currentMood.equals ("Amazing")) {
            moodScore = 30;
        }
        Log.i(TAG, "calcCurrentMoodScore: " + moodScore);
        return moodScore;
    }

    public boolean onTouchEvent(MotionEvent touchEvent){
        switch(touchEvent.getAction()){
            case MotionEvent.ACTION_DOWN:
                x1 = touchEvent.getX();
                y1 = touchEvent.getY();
                break;
            case MotionEvent.ACTION_UP:
                x2 = touchEvent.getX();
                y2 = touchEvent.getY();

                if(x1 > x2) {
                    Intent i = new Intent(QuoteActivity.this, HomeActivity.class);
//                    Intent i = new Intent(MoodActivity.this, QuoteActivity.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    Log.i("swiped left", "it worked");
                    //finish();
                }
                break;
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbarmenu, menu);
        return true;
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
        // setVisible(false);
        //finish();
    }

    public void onSettings(MenuItem item) {
        Intent intent = new Intent(QuoteActivity.this, SettingsActivity.class);
        startActivity(intent);
        //finish();
    }
}