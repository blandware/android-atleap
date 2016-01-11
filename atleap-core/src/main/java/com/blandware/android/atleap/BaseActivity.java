package com.blandware.android.atleap;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Use this activity as base for all. This class allows to use ActivityLifecycleCallbacks.
 */
public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getApplication() instanceof BaseApplication) {
            ((BaseApplication)getApplication()).dispatchActivityCreatedSupport(this, savedInstanceState);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (getApplication() instanceof BaseApplication) {
            ((BaseApplication)getApplication()).dispatchActivityStartedSupport(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (getApplication() instanceof BaseApplication) {
            ((BaseApplication)getApplication()).dispatchActivityResumedSupport(this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (getApplication() instanceof BaseApplication) {
            ((BaseApplication)getApplication()).dispatchActivityPausedSupport(this);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (getApplication() instanceof BaseApplication) {
            ((BaseApplication)getApplication()).dispatchActivityStoppedSupport(this);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (getApplication() instanceof BaseApplication) {
            ((BaseApplication)getApplication()).dispatchActivitySaveInstanceStateSupport(this, outState);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (getApplication() instanceof BaseApplication) {
            ((BaseApplication)getApplication()).dispatchActivityDestroyedSupport(this);
        }
    }
}
