package com.blandware.android.atleap.util;

/**
 * Created by agrebnev on 27.04.14.
 */

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.v4.util.LruCache;
import android.text.TextPaint;
import android.text.style.MetricAffectingSpan;

/**
 * This class allow to apply typeface for any spannable
 *
 * @author Tristan Waddington
 * https://gist.github.com/twaddington/b91341ea5615698b53b8
 */
public class TypefaceSpan extends MetricAffectingSpan {

    private static LruCache<String, Typeface> sTypefaceCache = new LruCache<String, Typeface>(3);

    private Typeface mTypeface;

    /**
     * Create instance
     * @param context context
     * @param typefaceName full path to font in the assets
     */
    public TypefaceSpan(Context context, String typefaceName) {
        mTypeface = sTypefaceCache.get(typefaceName);

        if (mTypeface == null) {
            mTypeface = Typeface.createFromAsset(context.getApplicationContext().getAssets(), typefaceName);

            // Cache the loaded Typeface
            sTypefaceCache.put(typefaceName, mTypeface);
        }
    }

    @Override
    public void updateMeasureState(TextPaint p) {
        p.setTypeface(mTypeface);

        // Note: This flag is required for proper typeface rendering
        p.setFlags(p.getFlags() | Paint.SUBPIXEL_TEXT_FLAG);
    }

    @Override
    public void updateDrawState(TextPaint tp) {
        tp.setTypeface(mTypeface);

        // Note: This flag is required for proper typeface rendering
        tp.setFlags(tp.getFlags() | Paint.SUBPIXEL_TEXT_FLAG);
    }
}
