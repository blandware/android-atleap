/*
 * Copyright (C) 2013 Blandware (http://www.blandware.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.blandware.android.atleap.sample.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.view.MenuItem;
import android.view.Window;

import com.blandware.android.atleap.BaseActivity;
import com.blandware.android.atleap.auth.AuthHelper;
import com.blandware.android.atleap.sample.Constants;
import com.blandware.android.atleap.sample.R;
import com.blandware.android.atleap.util.NavUtil;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_main);

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(new CalligraphyContextWrapper(newBase));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                boolean isBacked = NavUtil.backToRootFragment(this);
                if (!isBacked) {
                    Intent upIntent = NavUtils.getParentActivityIntent(this);
                    if (upIntent != null) {
                        if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
                            TaskStackBuilder.create(this)
                                    .addNextIntentWithParentStack(upIntent)
                                    .startActivities();
                        } else {
                            NavUtils.navigateUpTo(this, upIntent);
                        }
                    } else {
                        return false;
                    }
                }
                return true;
            }
            default: {
                return super.onOptionsItemSelected(item);
            }
        }
    }


    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            super.onBackPressed();
        } else {
            finish();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        boolean isAuthenticated = AuthHelper.checkLastAccountAndToken(Constants.ACCOUNT_TYPE, Constants.ACCOUNT_TOKEN_TYPE, null, null, this);
        if (isAuthenticated) {
            setUpNavigationDrawer(new DrawerFragment());
        } else {
            setUpNavigationDrawer(null);
        }
    }

    protected void setUpNavigationDrawer(Fragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        if (fragment != null) {
            manager.beginTransaction()
                    .replace(R.id.navigation_drawer, fragment)
                    .commit();
        } else {
            fragment = manager.findFragmentById(R.id.navigation_drawer);
            if (fragment != null) {
                manager.beginTransaction()
                        .remove(fragment)
                        .commit();
            }

            //clean container too
            fragment = manager.findFragmentById(R.id.container);
            if (fragment != null) {
                manager.beginTransaction()
                        .remove(fragment)
                        .commit();
            }

        }
    }
}
