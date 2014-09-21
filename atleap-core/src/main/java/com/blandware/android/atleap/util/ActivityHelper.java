package com.blandware.android.atleap.util;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;

/**
 * This class contains useful methods to work with Activity
 */
public class ActivityHelper {

    /**
     * Change specified Progress Bar Visibility or Indeterminate Progress Bar
     * @param activity activity
     * @param show <code>true</code> if we would like to show
     */
    public static void changeProgressBarVisibility(Activity activity, boolean show) {
        ProgressBar progressBar = (ProgressBar)activity.findViewById(android.R.id.progress);
        changeProgressBarVisibility(progressBar, activity, show);
    }

    /**
     * Change specified Progress Bar Visibility or Indeterminate Progress Bar
     * @param fragment fragment
     * @param show <code>true</code> if we would like to show
     */
    public static void changeProgressBarVisibility(Fragment fragment, boolean show) {
        ProgressBar progressBar = (ProgressBar)fragment.getView().findViewById(android.R.id.progress);
        changeProgressBarVisibility(progressBar, fragment.getActivity(), show);
    }

    /**
     * Change specified Progress Bar Visibility or Indeterminate Progress Bar
     * @param progressBar progress bar, could be <code>null</code>
     * @param show <code>true</code> if we would like to show
     */
    public static void changeProgressBarVisibility(ProgressBar progressBar, Activity activity, boolean show) {
        if (progressBar != null) {
            if (show)
                progressBar.setVisibility(View.VISIBLE);
            else
                progressBar.setVisibility(View.GONE);
        } else {
            if (activity != null) {
                if (activity instanceof ActionBarActivity) {
                    ActionBarActivity actionBarActivity = (ActionBarActivity) activity;
                    actionBarActivity.setSupportProgressBarIndeterminateVisibility(show);
                } else {
                    activity.setProgressBarIndeterminateVisibility(show);
                }
            }
        }
    }

    /**
     * Change title in the Action Bar
     * @param activity activity
     * @param title title of Action Bar
     */
    public static void changeActionBarTitle(Activity activity, String title) {
        changeActionBarTitle(activity, title, null);
    }

    /**
     * Change title in the Action Bar
     * @param activity activity
     * @param title title of Action Bar
     * @param typefaceName the full path to font in the assets
     */
    public static void changeActionBarTitle(Activity activity, String title, String typefaceName) {
        if (activity == null)
            return;

        if (!(activity instanceof ActionBarActivity))
            return;

        ActionBar actionBar = ((ActionBarActivity) activity).getSupportActionBar();
        if (actionBar != null) {
            if (title != null) {
                if (!TextUtils.isEmpty(typefaceName)) {
                    SpannableString spannableTitle = new SpannableString(title);
                    spannableTitle.setSpan(new TypefaceSpan(activity, typefaceName), 0, spannableTitle.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    actionBar.setTitle(spannableTitle);
                } else {
                    actionBar.setTitle(title);
                }
            }
        }
    }

}
