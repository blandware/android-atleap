package com.blandware.android.atleap.drawer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.blandware.android.atleap.util.NavUtil;

/**
 * This is the base implementation of the DrawerMenuItem
 */
public class DefaultDrawerMenuItem implements BaseDrawerFragment.DrawerMenuItem {

    private static final String TAG = DefaultDrawerMenuItem.class.getSimpleName();


    /**
     * Create menu item which instantiate new fragment and replace with it the container
     * @param fragmentClazz fragment class
     * @param containerId container id for the fragment replacement
     * @param title title string id
     */
    public DefaultDrawerMenuItem(Class<? extends Fragment> fragmentClazz, int containerId, int title) {
        this(fragmentClazz, containerId, title, 0, null);
    }

    /**
     * Create menu item which instantiate new fragment and replace with it the container
     * @param fragmentClazz fragment class
     * @param containerId container id for the fragment replacement
     * @param title title string id
     * @param icon drawable resource id
     */
    public DefaultDrawerMenuItem(Class<? extends Fragment> fragmentClazz, int containerId, int title, int icon) {
        this(fragmentClazz, containerId, title, icon, null);
    }

    /**
     * Create menu item which instantiate new fragment and replace with it the container
     * @param fragmentClazz fragment class
     * @param containerId container id for the fragment replacement
     * @param title title string id
     * @param icon drawable resource id
     * @param arguments arguments to the fragment
     */
    public DefaultDrawerMenuItem(Class<? extends Fragment> fragmentClazz, int containerId, int title, int icon, Bundle arguments) {
        this.mFragmentClazz = fragmentClazz;
        this.mContainerId = containerId;
        this.mTitleStringId = title;
        this.mArguments = arguments;
        this.mIconResourceId = icon;
    }

    /**
     * Create menu item which start new activity with specified intent
     * @param intent activity intent
     * @param title title string id
     * @param icon drawable resource id
     */
    public DefaultDrawerMenuItem(Intent intent, int title, int icon) {
        this.mTitleStringId = title;
        this.mIntent = intent;
        this.mIconResourceId = icon;
    }


    /**
     * Create menu item which start new activity with specified intent
     * @param intent activity intent
     * @param title title string id
     */
    public DefaultDrawerMenuItem(Intent intent, int title) {
        this(intent, title, 0);
    }

    /**
     * Create menu item which call callback when the user click on menu item
     * @param callback callback
     * @param title title string id
     * @param icon drawable resource id
     */
    public DefaultDrawerMenuItem(OnItemClickedCallback callback, int title, int icon) {
        this.mTitleStringId = title;
        this.mIconResourceId = icon;
        this.mCallback = callback;
    }


    /**
     * Create menu item which call callback when the user click on menu item
     * @param callback callback
     * @param title title string id
     */
    public DefaultDrawerMenuItem(OnItemClickedCallback callback, int title) {
        this(callback, title, 0);
    }

    protected int mContainerId = 0;
    protected Intent mIntent = null;
    protected Class<? extends Fragment> mFragmentClazz = null;
    protected int mTitleStringId = 0;
    protected Bundle mArguments = null;
    protected int mIconResourceId = 0;
    protected OnItemClickedCallback mCallback = null;

    public int getTitleStringId() {
        return mTitleStringId;
    }

    public int getIconResourceId() {
        return mIconResourceId;
    }

    public interface OnItemClickedCallback {
        public void onItemClicked(DefaultDrawerMenuItem menuItem);
    }


    @Override
    public boolean performMenuItem(FragmentActivity activity) {
        if (mFragmentClazz != null) {
            try {
                if (activity == null)
                    return false;

                final Fragment fragment = mFragmentClazz.newInstance();
                if (mArguments != null)
                    fragment.setArguments(mArguments);

                NavUtil.putFragmentIntoRoot(activity, mContainerId, fragment);

            } catch (InstantiationException ex) {
                Log.e(TAG, "Cannot create instance of fragment", ex);
                return false;
            } catch (IllegalAccessException ex) {
                Log.e(TAG, "Cannot create instance of fragment", ex);
                return false;
            }

            return true;

        } else if (mIntent != null) {
            if (activity == null)
                return false;

            activity.startActivity(mIntent);

            return true;

        } else if (mCallback != null) {
            mCallback.onItemClicked(this);

            return true;

        } else {
            return false;
        }
    }
}
