package com.example.gratitudejournal;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

@ParseClassName("Mentions")
public class Mentions extends ParseObject {
    public static final String KEY_FROMUSER = "fromUser";
    public static final String KEY_TOUSER = "toUser";
    public static final String KEY_ENTRY = "entry";



//    public Mentions () {
//        super();
//    }

    public String getFromUser() {
        return getString(KEY_FROMUSER);
    }

    public void setFromUser(String fromUser) {
        put(KEY_FROMUSER, fromUser);
    }

    public String getToUser() {
        return getString(KEY_TOUSER);
    }

    public void setToUser(String toUser) {
        put(KEY_TOUSER, toUser);
    }

    public String getMentionedEntry() {
        return getString(KEY_ENTRY);
    }

    public void setMentionedEntry(String entry) {
        put(KEY_ENTRY, entry);
    }

}
