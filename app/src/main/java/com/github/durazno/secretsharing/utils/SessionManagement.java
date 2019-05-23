package com.github.durazno.secretsharing.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * SessionManagement, stores the session data in SharedPreferences.
 */

public class SessionManagement {
    private static final String NAME = "PROJECTS";
    private static final String ID= "ID";
    private static final String KEY = "KEY";

    private final SharedPreferences sharedPreferences;

    public SessionManagement(Context context, String i) {
        sharedPreferences = context.getSharedPreferences(NAME+i, Context.MODE_PRIVATE);
    }

    public SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }

    public void setID(String userData){
        sharedPreferences.edit().putString(ID, userData).apply();
    }

    public void setKey(String userData){
        sharedPreferences.edit().putString(KEY, userData).apply();
    }

    public String getID(){
        return sharedPreferences.getString(ID, "");
    }

    public String getKey(){
        return sharedPreferences.getString(KEY, "");
    }
}
