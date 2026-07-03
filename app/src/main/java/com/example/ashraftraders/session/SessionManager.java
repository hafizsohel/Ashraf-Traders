package com.example.ashraftraders.session;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {

    private static final String PREF_NAME = "AshrafTraders";
    private static final String KEY_LOGIN = "login";
    private static final String KEY_ID = "id";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_ROLE = "role";

    private final SharedPreferences preferences;

    public SessionManager(Context context) {
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void saveLogin(long id, String username, String role) {
        preferences.edit()
                .putBoolean(KEY_LOGIN, true)
                .putLong(KEY_ID, id)
                .putString(KEY_USERNAME, username)
                .putString(KEY_ROLE, role)
                .apply();
    }

    public boolean isLogin() {
        return preferences.getBoolean(KEY_LOGIN, false);
    }

    public long getUserId() {
        return preferences.getLong(KEY_ID, 0);
    }

    public String getUsername() {
        return preferences.getString(KEY_USERNAME, "");
    }

    public String getRole() {
        return preferences.getString(KEY_ROLE, "");
    }

    public void logout() {
        preferences.edit().clear().apply();
    }
}