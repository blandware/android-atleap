package com.blandware.android.atleap;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

/**
 * Use this activity as base for all. This class allows to use ActivityLifecycleCallbacks.
 */
public class BaseActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getApplication() instanceof BaseApplication) {
            ((BaseApplication)getApplication()).dispatchActivityCreated(this, savedInstanceState);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (getApplication() instanceof BaseApplication) {
            ((BaseApplication)getApplication()).dispatchActivityStarted(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (getApplication() instanceof BaseApplication) {
            ((BaseApplication)getApplication()).dispatchActivityResumed(this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (getApplication() instanceof BaseApplication) {
            ((BaseApplication)getApplication()).dispatchActivityPaused(this);
        }
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
}
