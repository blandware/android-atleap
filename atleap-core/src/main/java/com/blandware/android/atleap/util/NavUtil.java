package com.blandware.android.atleap.util;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

/**
 * This class has methods to help navigation using fragments
 */
public class NavUtil {


    /**
     * Back to the fragment which is in the root of backstack
     * @param activity activity to take support fragment manager
     */
    public static boolean backToRootFragment(FragmentActivity activity) {
        if (activity != null) {
            return backToRootFragment(activity.getSupportFragmentManager());
        } else {
            return false;
        }
    }

    /**
     * Back to the fragment which is in the root of backstack
     * @param manager
     */
    public static boolean backToRootFragment(FragmentManager manager) {
        return clearBackStack(manager, true);
    }


    /**
     * Put specified fragment to root of backstack
     * @param activity activity to take support fragment manager
     * @param containerId container where fragment should be replaced
     * @param fragment new fragment
     */
    public static void putFragmentIntoRoot(FragmentActivity activity, int containerId, Fragment fragment) {
        if (activity != null) {
            putFragmentIntoRoot(activity.getSupportFragmentManager(), containerId, fragment);
        }
    }

    /**
     * Put specified fragment to root of backstack
     * @param manager support fragment manager
     * @param containerId container where fragment should be replaced
     * @param fragment new fragment
     */
    public static void putFragmentIntoRoot(FragmentManager manager, int containerId, Fragment fragment) {
        clearBackStack(manager, false);
        replaceFragment(manager, containerId, fragment);
    }

    /**
     * Replace the current fragment with specified one in specified container
     * @param activity activity to take support fragment manager
     * @param containerId container where fragment should be replaced
     * @param fragment new fragment
     */
    public static void replaceFragment(FragmentActivity activity, int containerId, Fragment fragment) {

        if (activity != null) {
            replaceFragment(activity.getSupportFragmentManager(), containerId, fragment);
        }
    }

    /**
     * Replace the current fragment with specified one in specified container
     * @param manager support fragment manager
     * @param containerId container where fragment should be replaced
     * @param fragment new fragment
     */
    public static void replaceFragment(FragmentManager manager, int containerId, Fragment fragment) {
        manager.beginTransaction()
                .replace(containerId, fragment)
                .addToBackStack(null)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    /**
     * Clear back stack of specified fragment manager
     * @param manager support fragment manager
     * @param exclusive <code>true</code> if the back stack should be cleared but stayed the first element
     * @return <code>true</code> if the first element was not cleared
     */
    public static boolean clearBackStack(FragmentManager manager, boolean exclusive) {
        int stackSize = manager.getBackStackEntryCount();
        if ((stackSize == 0) || ((stackSize == 1) && exclusive)) {
            return false;
        }

        int id = manager.getBackStackEntryAt(0).getId();
        manager.popBackStack(id, exclusive ? 0 : FragmentManager.POP_BACK_STACK_INCLUSIVE);
        return true;
    }

}
