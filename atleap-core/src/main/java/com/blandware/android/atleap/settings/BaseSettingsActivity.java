package com.blandware.android.atleap.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

import com.blandware.android.atleap.BaseApplication;

import java.util.HashMap;

/**
 * Created by agrebnev on 01.05.14.
 */
public abstract class BaseSettingsActivity extends PreferenceActivity implements
        SharedPreferences.OnSharedPreferenceChangeListener {
    protected static final String TAG = BaseSettingsActivity.class.getSimpleName();

    private HashMap<String, PreferenceHolder> preferenceListeners = new HashMap<String, PreferenceHolder>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(getPreferencesResource());

        if (getApplication() instanceof BaseApplication) {
            ((BaseApplication)getApplication()).dispatchActivityCreated(this, savedInstanceState);
        }
    }

    protected abstract int getPreferencesResource();

    public void registerOnPreferenceListener(String key, OnPreferenceListener listener) {

        Preference preference = findPreference(key);

        if (preference == null) {
            throw new IllegalArgumentException("There are no preference with key = " + key);
        }

        PreferenceHolder holder = new PreferenceHolder();
        holder.listener = listener;
        holder.preference = preference;

        preferenceListeners.put(key, holder);

        listener.onInit(preference, getPreferenceManager());
    }



    @Override
    protected void onStart() {
        super.onStart();
        if (getApplication() instanceof BaseApplication) {
            ((BaseApplication)getApplication()).dispatchActivityStarted(this);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (getApplication() instanceof BaseApplication) {
            ((BaseApplication)getApplication()).dispatchActivityResumed(this);
        }

        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);

        for(String key:preferenceListeners.keySet()) {
            PreferenceHolder holder = preferenceListeners.get(key);
            holder.preference.setOnPreferenceChangeListener(holder.listener);
        }

    }

    @Override
    public void onPause() {
        super.onPause();

        if (getApplication() instanceof BaseApplication) {
            ((BaseApplication)getApplication()).dispatchActivityPaused(this);
        }

        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);

        for(String key:preferenceListeners.keySet()) {
            PreferenceHolder holder = preferenceListeners.get(key);
            holder.preference.setOnPreferenceChangeListener(null);
        }

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
                                          String key) {

    }


    @Override
    protected void onStop() {
        super.onStop();
        if (getApplication() instanceof BaseApplication) {
            ((BaseApplication)getApplication()).dispatchActivityStopped(this);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (getApplication() instanceof BaseApplication) {
            ((BaseApplication)getApplication()).dispatchActivitySaveInstanceState(this, outState);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (getApplication() instanceof BaseApplication) {
            ((BaseApplication)getApplication()).dispatchActivityDestroyed(this);
        }
    }

    public interface OnPreferenceListener extends Preference.OnPreferenceChangeListener {

        public void onInit(Preference preference, PreferenceManager preferenceManager);

    }

    protected class PreferenceHolder {
        public OnPreferenceListener listener;
        public Preference preference;
    }
}