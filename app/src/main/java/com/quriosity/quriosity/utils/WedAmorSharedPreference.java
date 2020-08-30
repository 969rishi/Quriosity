package com.quriosity.quriosity.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;


public class WedAmorSharedPreference {

    private static final String STORE_NAME = "wedding_config";
    private SharedPreferences prefs;
    private boolean useApply;

    public WedAmorSharedPreference(Context context) {
        prefs = context.getSharedPreferences(STORE_NAME, Context.MODE_PRIVATE);
        boolean useApply = true;
        try {
            Editor.class.getMethod("apply");
        } catch (NoSuchMethodException e) {
            useApply = false;
        }
        this.useApply = useApply;
    }

    public void saveString(SharePrefKey key, String value) {
        saveChanges(prefs.edit().putString(key.key, value));
    }

    public void saveInt(SharePrefKey key, int value) {
        saveChanges(prefs.edit().putInt(key.key, value));
    }

    public void saveBoolean(SharePrefKey key, boolean value) {
        saveChanges(prefs.edit().putBoolean(key.key, value));
    }

    public String getString(SharePrefKey sharePrefKey, String defaultValue) {
        return prefs.getString(sharePrefKey.key, defaultValue);
    }

    public int getInt(SharePrefKey sharePrefKey, int defaultValue) {
        return prefs.getInt(sharePrefKey.key, defaultValue);
    }

    public long getLong(SharePrefKey sharePrefKey, long defaultValue) {
        return prefs.getLong(sharePrefKey.key, defaultValue);
    }

    public boolean getBoolean(SharePrefKey sharePrefKey, boolean defaultValue) {
        return prefs.getBoolean(sharePrefKey.key, defaultValue);
    }

    public void saveLong(SharePrefKey key, long value) {
        saveChanges(prefs.edit().putLong(key.key, value));
    }

    private void saveChanges(Editor editor) {
        editor.commit();
    }

    public enum SharePrefKey {
        USER_LOGIN_STATUS("USER_LOGIN"),
        PRIMARY_EMAIL_ID("PRIMARY_EMAIL_ID"),
        USER_IMAGE("USER_IMAGE"),
        USER_FIRST_NAME("USER_FIRST_NAME"),
        USER_LAST_NAME("USER_LAST_NAME"),
        FIREBASE_UID("FIREBASE_USER_UID"),
        FIRST_INSTALL("FIRST_INSTALL"),
        isAPPFORGROUND("isAppinForeground"),
        CURRENT_ACTIVE_CHATID("current_active_chat_id"),


        PHONE_NO("PHONE_NO"),
        PHONE_NO_CC("PHONE_NO_country_code"),
        FIREBASE_REGISTRATION_TOKEN("Firebase_register_token");

        public final String key;

        SharePrefKey(String key) {
            this.key = key;
        }
    }
}
