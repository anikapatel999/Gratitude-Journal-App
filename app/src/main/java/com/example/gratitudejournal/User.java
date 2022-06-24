package com.example.myapplication;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.json.JSONArray;

import java.util.List;
import com.example.myapplication.Entry;

@ParseClassName("_User") //SHOULD PROBABLY HAVE STAYED AS USER INSTEAD OF _USER
public class User extends ParseUser { //or is it ParseObject? does it matter?
    public static final String KEY_MOODS = "moods";
    public static final String KEY_CURRENTENTRY = "currentEntry";
    public static final String KEY_FRIENDS = "friends";
    public static final String KEY_CLOSEFRIENDS = "closeFriends";
    public static final String KEY_VISFRIENDMENTIONS = "visFriendMentions";
    public static final String KEY_VISCLOSEFRIENDMENTIONS = "visCloseFriendMentions";
    public static final String KEY_TIMEZONE = "timeZone";


    public static final String KEY_USER = "user";

//    public User(ParseUser user) {
//        super();
//    }

    public JSONArray getMoods() {
        return getJSONArray(KEY_MOODS);
    }

    public void setMoods(JSONArray mood) {
        put(KEY_MOODS, mood);
    }

    public Entry getCurrentEntry() {
        return (Entry) getParseObject(KEY_CURRENTENTRY);
    }

    public void setCurrentEntry(Entry currentEntry) {
        put(KEY_CURRENTENTRY, currentEntry);
    }

    public JSONArray getFriends() {
        return getJSONArray(KEY_FRIENDS);
    }

    public void setFriends(JSONArray friends) {
        put(KEY_FRIENDS, friends);
    }

    public JSONArray getCloseFriends() {
        return getJSONArray(KEY_CLOSEFRIENDS);
    }

    public void setCloseFriends(String closeFriends) {
        put(KEY_CLOSEFRIENDS, closeFriends);
    }

    public JSONArray getVisFriendMentions() {
        return getJSONArray(KEY_VISFRIENDMENTIONS);
    }

    public void setVisFriendMentions(JSONArray visFriendMentions) {
        put(KEY_VISFRIENDMENTIONS, visFriendMentions);
    }

    public JSONArray getVisCloseFriendMentions() {
        return getJSONArray(KEY_VISCLOSEFRIENDMENTIONS);
    }

    public void setVisCloseFriendMentions(JSONArray visCloseFriendMentions) {
        put(KEY_VISFRIENDMENTIONS, visCloseFriendMentions);
    }

    public void setTimeZone(String timezone) {
        put(KEY_TIMEZONE, timezone);
    }

    public String getTimeZone() {
        return getString(KEY_TIMEZONE);
    }

}
