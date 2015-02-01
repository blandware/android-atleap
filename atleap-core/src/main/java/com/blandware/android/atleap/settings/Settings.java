package com.blandware.android.atleap.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.blandware.android.atleap.AppContext;

import java.util.HashMap;
import java.util.Map;

/**
 * This is the helper for the PreferenceManager.getDefaultSharedPreferences.
 * It allows to work without context.
 */
public class Settings {

    private static final String MAP_DELIMITER = "___";

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

    public static int getInt(String key, int defaultValue) {
        return getSharedPreferences().getInt(key, defaultValue);
    }

    public static void putLong(String key, long value) {
        getSharedPreferences().edit().putLong(key, value).commit();
    }

    public static long getLong(String key, long defaultValue) {
        return getSharedPreferences().getLong(key, defaultValue);
    }

    public static void putDouble(String key, double value) {
        putLong(key, Double.doubleToRawLongBits(value));
    }

    public static double getDouble(String key, double defaultValue) {
        return Double.longBitsToDouble(getLong(key, Double.doubleToLongBits(defaultValue)));
    }

    /**
     * Replace old map with new one. The values will not be merged.
     * @param key key
     * @param map map (could be <code>null</code> in order to clean value)
     */
    public static void putMap(String key, Map<String, String> map) {
        Map<String, String> oldMap = getMap(key, null);

        SharedPreferences.Editor editor = getSharedPreferences().edit();
        //remove all old values
        if (oldMap != null && oldMap.size() != 0) {
            for (String oldKey:oldMap.keySet()) {
                editor.remove(oldKey);
            }
        }

        //add new value
        if (map != null && map.size() != 0) {
            for (String mapKey : map.keySet()) {
                editor.putString(key + MAP_DELIMITER + mapKey, map.get(mapKey));
            }
        }
        editor.commit();
    }

    public static Map<String, String> getMap(String key, Map<String, String> defaultMap) {
        Map<String, String> resultMap = new HashMap<String, String>();
        Map<String, Object> all = (Map<String, Object>)getSharedPreferences().getAll();

        if (all == null)
            return defaultMap;

        for (String mapKey: all.keySet()) {
            if (mapKey.startsWith(key)) {
                String shortKey = mapKey.split(MAP_DELIMITER)[1];
                resultMap.put(shortKey, String.valueOf(all.get(mapKey)));
            }
        }

        if (!resultMap.isEmpty()) {
            return resultMap;
        } else {
            return defaultMap;
        }
    }

    public static void remove(String key) {
        getSharedPreferences().edit().remove(key).commit();
    }

    public static boolean contains(String key) {
        return getSharedPreferences().contains(key);
    }

    public static void clear() {
        getSharedPreferences().edit().clear().commit();
    }

}
