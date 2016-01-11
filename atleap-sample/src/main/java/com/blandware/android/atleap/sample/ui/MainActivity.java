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

import android.accounts.Account;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.CookieManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.blandware.android.atleap.BaseActivity;
import com.blandware.android.atleap.auth.AuthHelper;
import com.blandware.android.atleap.sample.Constants;
import com.blandware.android.atleap.sample.R;
import com.blandware.android.atleap.sample.auth.AuthActivity;
import com.blandware.android.atleap.util.NavUtil;
import com.squareup.picasso.Picasso;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = MainActivity.class.getSimpleName();


    private NavigationView mNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //supportRequestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.activity_main_drawer_open, R.string.activity_main_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void setupNavigationView() {

        Log.d(TAG, "setupNavigationView");

        View view = mNavigationView.getHeaderView(0);

        Account account = AuthHelper.getLastOrFirstAccount(this, Constants.ACCOUNT_TYPE);

        Log.d(TAG, "Account " + account);

        ImageView userAvatarImageView = (ImageView)view.findViewById(R.id.user_avatar);
        Picasso.with(this).load(AuthHelper.getUserData(this, account, AuthActivity.KEY_USER_AVATAR_URL)).into(userAvatarImageView);

        TextView userNameTextView = (TextView)view.findViewById(R.id.user_name);
        userNameTextView.setText(AuthHelper.getUserData(this, account, AuthActivity.KEY_USER_NAME));

        TextView userLoginTextView = (TextView)view.findViewById(R.id.user_login);
        userLoginTextView.setText(account.name);

    }


    @Override
    protected void onStart() {
        super.onStart();
        boolean isAuthenticated = AuthHelper.checkLastAccountAndToken(Constants.ACCOUNT_TYPE, Constants.ACCOUNT_TOKEN_TYPE, null, null, this);
        if (isAuthenticated) {
            setupNavigationView();


            NavUtil.replaceFragmentIfNeeded(this, R.id.container, MasterFragment.class, null);

        }
    }


/*    @Override
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
        if (getSupportFragmentManager().findFragmentById(R.id.navigation_drawer) == null) {
            boolean isAuthenticated = AuthHelper.checkLastAccountAndToken(Constants.ACCOUNT_TYPE, Constants.ACCOUNT_TOKEN_TYPE, null, null, this);
            if (isAuthenticated) {
                setUpNavigationDrawer(new DrawerFragment());
            } else {
                setUpNavigationDrawer(null);
            }
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
    }*/

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_search) {
            // Handle the camera action
            NavUtil.replaceFragmentIfNeeded(this, R.id.container, MasterFragment.class, null);

        } else if (id == R.id.nav_logout) {
            logoutMenuItemClicked();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logoutMenuItemClicked() {
        AuthHelper.invalidateAuthTokenForLastAccount(Constants.ACCOUNT_TYPE, Constants.ACCOUNT_TOKEN_TYPE, null, null, this);
        CookieManager.getInstance().removeAllCookie();
    }


}
