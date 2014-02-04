package com.blandware.android.atleap;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * This is the helper for the PreferenceManager.getDefaultSharedPreferences.
 * It allows to work without context.
 */
public class Settings {

    public static SharedPreferences getSharedPreferences() {
        Context context = AppContext.getContext();
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static void putBoolean(String key, boolean value) {
        getSharedPreferences().edit().putBoolean(key, value).commit();
    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        return getSharedPreferences().getBoolean(key, defaultValue);
    }

    public static void putString(String key, String value) {
        getSharedPreferences().edit().putString(key, value).commit();
    }

    public static String getString(String key, String defaultValue) {
        return getSharedPreferences().getString(key, defaultValue);
    }

    public static void putFloat(String key, float value) {
        getSharedPreferences().edit().putFloat(key, value).commit();
    }

    public static float getFloat(String key, float defaultValue) {
        return getSharedPreferences().getFloat(key, defaultValue);
    }

    public static void putInt(String key, int value) {
        getSharedPreferences().edit().putInt(key, value).commit();
    }

    public static float getInt(String key, int defaultValue) {
        return getSharedPreferences().getInt(key, defaultValue);
    }

    public static void putLong(String key, long value) {
        getSharedPreferences().edit().putLong(key, value).commit();
    }

    public static float getLong(String key, long defaultValue) {
        return getSharedPreferences().getLong(key, defaultValue);
    }

}
