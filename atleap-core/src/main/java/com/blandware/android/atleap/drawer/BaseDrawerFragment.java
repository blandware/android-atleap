package com.blandware.android.atleap.drawer;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * Base Abstract fragment to implement drawer layout
 */
public abstract class BaseDrawerFragment extends Fragment implements FragmentManager.OnBackStackChangedListener {

    /**
     * Per the design guidelines, you should show the drawer on launch until the user manually
     * expands it. This shared preference tracks this.
     */
    protected static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";

    private static final String TAG = BaseDrawerFragment.class.getSimpleName();

    protected static final String STATE_IS_FIRST_CREATION = "STATE_IS_FIRST_CREATION";

    /**
     * A pointer to the current callbacks instance (the Activity).
     */
    protected NavigationDrawerCallbacks mCallbacks;

    /**
     * Helper component that ties the action bar to the navigation drawer.
     */
    protected ActionBarDrawerToggle mDrawerToggle;

    protected DrawerLayout mDrawerLayout;

    protected boolean mFromSavedInstanceState;
    protected boolean mUserLearnedDrawer;
    protected ListView mMenuListView;
    protected View mFragmentContainerView;
    protected ListAdapter mMenuListAdapter;

    protected MenuConfig mMenuConfig;
    protected DrawerConfig mDrawerConfig;



    /**
     * {@inheritDoc}
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Read in the flag indicating whether or not the user has demonstrated awareness of the
        // drawer. See PREF_USER_LEARNED_DRAWER for details.
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mUserLearnedDrawer = sp.getBoolean(PREF_USER_LEARNED_DRAWER, false);

        mDrawerConfig = getDrawerConfig();
        mMenuConfig = getMenuConfig();
        mMenuListAdapter = mMenuConfig.menuListAdapter;

        // Indicate that this fragment would like to influence the set of actions in the action bar.
        setHasOptionsMenu(true);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(mDrawerConfig.fragmentLayoutResourceId, container, false);

        if (mMenuConfig != null) {
            mMenuListView = (ListView) view.findViewById(mMenuConfig.menuListViewId);

            if (mMenuConfig.menuListAdapter != null) {
                mMenuListView.setAdapter(mMenuListAdapter);
            }

            mMenuListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    selectMenuItem(position);
                }
            });
            mMenuListView.setAdapter(mMenuConfig.menuListAdapter);
        }

        if (savedInstanceState == null) {
            selectMenuItem(getDefaultMenuItemPosition());
        }

        return view;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mFragmentContainerView = getActivity().findViewById(mDrawerConfig.fragmentContainerId);
        View view = getActivity().findViewById(mDrawerConfig.drawerLayoutViewId);
        if (view != null && view instanceof DrawerLayout) {
            mDrawerLayout = (DrawerLayout)view;
        }

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        if (mDrawerLayout != null) {

            // set a custom shadow that overlays the main content when the drawer opens
            if (mDrawerConfig.drawerShadowResourceId != 0)
                mDrawerLayout.setDrawerShadow(mDrawerConfig.drawerShadowResourceId, GravityCompat.START);

            // ActionBarDrawerToggle ties together the the proper interactions
            // between the navigation drawer and the action bar app mIconResourceId.
            mDrawerToggle = new ActionBarDrawerToggle(
                    getActivity(),                    /* host Activity */
                    mDrawerLayout,                    /* DrawerLayout object */
                    mDrawerConfig.drawableResourceId,             /* nav drawer image to replace 'Up' caret */
                    mDrawerConfig.drawerOpenStringId,  /* "open drawer" description for accessibility */
                    mDrawerConfig.drawerCloseStringId  /* "close drawer" description for accessibility */
            ) {
                @Override
                public void onDrawerClosed(View drawerView) {
                    super.onDrawerClosed(drawerView);
                    BaseDrawerFragment.this.onDrawerClosed(drawerView);
                }

                @Override
                public void onDrawerOpened(View drawerView) {
                    super.onDrawerOpened(drawerView);
                    BaseDrawerFragment.this.onDrawerOpened(drawerView);
                }
            };

            // If the user hasn't 'learned' about the drawer, open it to introduce them to the drawer,
            // per the navigation drawer design guidelines.
            if (!mUserLearnedDrawer && !mFromSavedInstanceState) {
                mDrawerLayout.openDrawer(mFragmentContainerView);
            }

            // Defer code dependent on restoration of previous instance state.
            mDrawerLayout.post(new Runnable() {
                @Override
                public void run() {
                    mDrawerToggle.syncState();
                }
            });

            mDrawerLayout.setDrawerListener(mDrawerToggle);
        }

        initUpIcon();

    }

    protected void onDrawerClosed(View drawerView) {
        if (!isAdded()) {
            return;
        }
        getActivity().supportInvalidateOptionsMenu(); // calls onPrepareOptionsMenu()
    }

    protected void onDrawerOpened(View drawerView) {
        if (!isAdded()) {
            return;
        }

        if (!mUserLearnedDrawer) {
            // The user manually opened the drawer; store this flag to prevent auto-showing
            // the navigation drawer automatically in the future.
            mUserLearnedDrawer = true;
            SharedPreferences sp = PreferenceManager
                    .getDefaultSharedPreferences(getActivity());
            sp.edit().putBoolean(PREF_USER_LEARNED_DRAWER, true).apply();
        }
        getActivity().supportInvalidateOptionsMenu(); // calls onPrepareOptionsMenu()
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle != null && mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Change mIconResourceId at the UP place
     */
    public void setDisplayHomeAsUpEnabled(boolean value) {
        if (mDrawerToggle != null)
            mDrawerToggle.setDrawerIndicatorEnabled(!value);
        else {
            ActionBar actionBar = getActionBar();
            if (actionBar != null)
                actionBar.setDisplayHomeAsUpEnabled(value);
        }
    }


    public void selectMenuItem(int position) {
        if (mMenuListView != null) {
            mMenuListView.setItemChecked(position, true);
        }
        if (mMenuListAdapter != null && getActivity() != null) {
            Object o = mMenuListAdapter.getItem(position);
            if (o instanceof DrawerMenuItem) {
                DrawerMenuItem menuItem = (DrawerMenuItem)o;
                menuItem.performMenuItem(getActivity());
            } else {
                throw new ClassCastException("MenuItems must implement DrawerMenuItem");
            }
        }
        if (mDrawerLayout != null) {
            mDrawerLayout.closeDrawer(mFragmentContainerView);
        }
        if (mCallbacks != null) {
            mCallbacks.onNavigationDrawerItemSelected(position);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (!(activity instanceof ActionBarActivity)) {
            throw new ClassCastException("Activity must extend ActionBarActivity");
        }

        if (activity instanceof NavigationDrawerCallbacks) {
            mCallbacks = (NavigationDrawerCallbacks) activity;
        }

        getActivity().getSupportFragmentManager().addOnBackStackChangedListener(this);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void onDetach() {
        getActivity().getSupportFragmentManager().removeOnBackStackChangedListener(this);
        super.onDetach();
        mCallbacks = null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onBackStackChanged() {
        initUpIcon();
    }

    /**
     * Setup icon for the Up place
     */
    protected void initUpIcon() {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            if (activity.getSupportFragmentManager().getBackStackEntryCount() > 1)
                setDisplayHomeAsUpEnabled(true);
            else
                setDisplayHomeAsUpEnabled(false);
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(STATE_IS_FIRST_CREATION, false);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Forward the new configuration the drawer toggle component.
        if (mDrawerToggle != null)
            mDrawerToggle.onConfigurationChanged(newConfig);
    }

    public boolean isDrawerOpen() {
        return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(mFragmentContainerView);
    }

    public DrawerLayout getDrawerLayout() {
        return mDrawerLayout;
    }

    protected ActionBar getActionBar() {
        ActionBarActivity activity = (ActionBarActivity)getActivity();
        if (activity != null)
            return activity.getSupportActionBar();
        else
            return null;
    }

    /**
     * Configure navigation drawer
     * @return drawer config
     */
    public abstract DrawerConfig getDrawerConfig();

    /**
     * Implement this method if you would like to use menu inside drawer layout
     * @return menu config
     */
    public MenuConfig getMenuConfig() {
        return null;
    }


    /**
     * Returns default menu item position for the first drawer creation
     * @return default menu item position for the first drawer creation
     */
    protected int getDefaultMenuItemPosition() {
        return 0;
    }

    /**
     * Callbacks interface that all activities using this fragment must implement.
     */
    public static interface NavigationDrawerCallbacks {
        /**
         * Called when an item in the navigation drawer is selected.
         */
        void onNavigationDrawerItemSelected(int position);
    }

    public static class DrawerConfig {
        public int drawerLayoutViewId;
        public int fragmentLayoutResourceId;
        public int fragmentContainerId;

        public int drawableResourceId;
        public int drawerShadowResourceId = 0;
        public int drawerOpenStringId;
        public int drawerCloseStringId;
    }

    public static class MenuConfig {
        public int menuListViewId;
        public ListAdapter menuListAdapter;
    }

    /**
     * Interface that all menu item must implement.
     */
    public interface DrawerMenuItem {
        /**
         * Called when user clicked at menu item
         * @param activity current activity
         * @return <code>true</code> if menu item action performed successfully
         */
        public boolean performMenuItem(FragmentActivity activity);
    }
}
