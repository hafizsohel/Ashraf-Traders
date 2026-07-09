package com.example.ashraftraders.session;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class SessionManager {

    private static final String PREF_NAME = "AshrafTraders";
    private static final String KEY_LOGIN = "login";
    private static final String KEY_ID = "id";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_ROLE = "role";
    private static final String KEY_PROFILE_IMAGE = "profile_image";
    private static final String KEY_FULLNAME = "full_name";

    private final SharedPreferences preferences;

    public SessionManager(Context context) {
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }


    public void saveLogin(long id, String username,String fullName, String role, String profileImage ) {
        Log.d("LOGIN", "saveLogin "+fullName);
        preferences.edit()
                .putBoolean(KEY_LOGIN, true)
                .putLong(KEY_ID, id)
                .putString(KEY_USERNAME, username)
                .putString(KEY_ROLE, role)
                .putString(KEY_FULLNAME,fullName)
                .putString(KEY_PROFILE_IMAGE, profileImage)
                .apply();
    }

    public String getProfileImage() {
        return preferences.getString(KEY_PROFILE_IMAGE, "");
    }
    public String getFullName() {
        return preferences.getString(KEY_FULLNAME, "");
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