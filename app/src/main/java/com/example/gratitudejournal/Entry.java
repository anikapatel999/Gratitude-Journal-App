package com.example.myapplication;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

@ParseClassName("Entry")
public class Entry extends ParseObject {
    public static final String KEY_MOOD = "mood";
    public static final String KEY_USER = "user";
    public static final String KEY_TEXT = "text";
    public static final String KEY_FRIENDMENTIONS = "friendMentions";
    public static final String KEY_CLOSEFRIENDMENTIONS = "closeFriendMentions";



//    public Entry () {
//        super();
//    }

    public String getMood() {
        return getString(KEY_MOOD);
    }

    public void setMood(String mood) {
        put(KEY_MOOD, mood);
    }

    public String getText() {
        return getString(KEY_TEXT);
    }

    public void setText(String text) {
        put(KEY_TEXT, text);
    }

    public ParseUser getUser() {
        return getParseUser(KEY_USER);
    }

    public void setUser(ParseUser user) {
        put(KEY_USER, user);
    }

    public JSONArray getFriendMentions() {
        return getJSONArray(KEY_FRIENDMENTIONS);
    }

    public void setFriendMentions(ArrayList<String> friendMentions) {
        put(KEY_FRIENDMENTIONS, friendMentions);
    }

    public JSONArray getCloseFriendMentions() {
        return getJSONArray(KEY_CLOSEFRIENDMENTIONS);
    }

    public void setCloseFriendMentions(ArrayList<String> closeFriendMentions) {
        put(KEY_CLOSEFRIENDMENTIONS, closeFriendMentions);
    }


}
