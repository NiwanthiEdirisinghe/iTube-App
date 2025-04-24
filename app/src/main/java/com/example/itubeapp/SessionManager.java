package com.example.itubeapp;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context context;

    private static final String PREF_NAME = "iTubePrefs";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_USER_ID = "userId";
    private static final String KEY_USERNAME = "username";

    public SessionManager(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public void createLoginSession(long userId, String username) {
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.putLong(KEY_USER_ID, userId);
        editor.putString(KEY_USERNAME, username);
        editor.commit();
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public long getUserId() {
        return pref.getLong(KEY_USER_ID, -1);
    }

    public String getUsername() {
        return pref.getString(KEY_USERNAME, null);
    }

    public void logout() {
        editor.clear();
        editor.commit();
    }
}
