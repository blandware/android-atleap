package com.blandware.android.atleap.util;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * This class provides methods for show/hide soft input (soft keyboard)
 */
public class SoftInputUtil {

    /**
     * Show keyboard
     * @param context context
     * @param view The currently focused view, which would like to receive
     * soft keyboard input.
     */
    public static void showSoftInput(Context context, View view) {
        if (view == null)
            return;

        InputMethodManager inputMethodManager = (InputMethodManager)
                context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(view, 0);
    }

    /**
     * Hide keyboard
     * @param context context
     * @param view The currently focused view, which would like to receive
     * soft keyboard input.
     */
    public static void hideSoftInput(Context context, View view) {
        if (view == null)
            return;

        InputMethodManager inputMethodManager = (InputMethodManager)
                context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
