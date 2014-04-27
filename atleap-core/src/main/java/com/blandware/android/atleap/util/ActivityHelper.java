package com.blandware.android.atleap.util;

import android.app.Activity;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;

/**
 * This class contains useful methods to work with Activity
 */
public class ActivityHelper {

    /**
     * Change Progress Bar Indeterminate Visibility
     * @param activity activity
     * @param show <code>true</code> if we would like to show
     */
    public static void changeProgressBarVisibility(Activity activity, boolean show) {
        if (activity instanceof ActionBarActivity) {
            final ActionBarActivity actionBarActivity = (ActionBarActivity) activity;
            if (actionBarActivity != null) {
                actionBarActivity.setSupportProgressBarIndeterminateVisibility(show);
            }
        }
    }

    /**
     * Change title in the Action Bar
     * @param activity activity
     * @param titleStringRes resource of String title
     */
    public static void changeActionBarTitle(Activity activity, int titleStringRes) {
        changeActionBarTitle(activity, titleStringRes, null);
    }

    /**
     * Change title in the Action Bar
     * @param activity activity
     * @param titleStringRes resource of String title
     * @param typefaceName the full path to font in the assets
     */
    public static void changeActionBarTitle(Activity activity, int titleStringRes, String typefaceName) {
        if (!(activity instanceof ActionBarActivity))
            return;

        ActionBar actionBar = ((ActionBarActivity) activity).getSupportActionBar();
        if (actionBar != null) {
            if (titleStringRes != 0) {
                String title = activity.getString(titleStringRes);
                if (!TextUtils.isEmpty(typefaceName)) {
                    SpannableString spannableTitle = new SpannableString(title);
                    spannableTitle.setSpan(new TypefaceSpan(activity, typefaceName), 0, spannableTitle.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    actionBar.setTitle(spannableTitle);
                } else {
                    actionBar.setTitle(title);
                }

            } else {
                actionBar.setTitle("");
            }
        }
    }

}
