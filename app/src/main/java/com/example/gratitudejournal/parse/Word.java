package com.example.gratitudejournal.parse;

import android.annotation.SuppressLint;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class Word {
    ArrayList<String> s;

    @SuppressLint("NotConstructor")
    public ArrayList<String> Word(JSONArray j5) throws JSONException {
        for (int index = 0; index < j5.length(); index++) {
            // Log.i(TAG, ":) " + j5.get(index));
            s.add(j5.get(index).toString());
        }
        return s;
    }

    public ArrayList<String> getSynonyms() {
        return s;
    }

}
