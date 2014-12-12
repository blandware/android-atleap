package com.blandware.android.atleap.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

/**
 * This class provides tabs strip for Pager with the possibility to provide custom View for every tab.
 * This class extends {@link android.widget.HorizontalScrollView} so you can use its attributes.
 * This view can be placed everywhere, however it is not attached to ViewPager in case of using inside it.
 * Use {@link #setViewPager(android.support.v4.view.ViewPager)} method instead.
 */
public class PagerViewStrip extends HorizontalScrollView {

    protected ViewPager mViewPager;
    protected ViewPager.OnPageChangeListener mDelegatePageChangeListener;
    protected LinearLayout mLinearLayout;

    protected int lastScroll = 0;

    protected static final int SCROLL_OFFSET = 50;

    /**
     * Create PagerViewStrip
     * @param context context
     */
    public PagerViewStrip(Context context) {
        this(context, null);
    }

    /**
     * Create PagerViewStrip
     * @param context context
     * @param attrs attributes
     */
    public PagerViewStrip(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * Create PagerViewStrip
     * @param context context
     * @param attrs attributes
     * @param defStyle style
     */
    public PagerViewStrip(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        mLinearLayout = new LinearLayout(context, null);
        mLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
        mLinearLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        addView(mLinearLayout);
    }

    /**
     * Set ViewPager
     * @param viewPager ViewPager
     */
    public void setViewPager(ViewPager viewPager) {
        if (viewPager.getAdapter() == null) {
            throw new IllegalStateException("ViewPager has to have adapter");
        }

        if (!(viewPager.getAdapter() instanceof TabPagerAdapter)) {
            throw new IllegalStateException("Adapter has to implement TabPagerAdapter interface");
        }

        mViewPager = viewPager;
        mViewPager.setOnPageChangeListener(new InternalPageChangeListener());

        refreshData(0);
    }


    /**
     * Set listener
     * @param onPageChangeListener OnPageChangeListener
     */
    public void setOnPageChangeListener(ViewPager.OnPageChangeListener onPageChangeListener) {
        this.mDelegatePageChangeListener = onPageChangeListener;
    }

    protected void scrollView(int position, int offset) {
        if (mViewPager.getAdapter().getCount() == 0) {
            return;
        }

        int newScrollX = mLinearLayout.getChildAt(position).getLeft() + offset;

        if (position > 0 || offset > 0) {
            newScrollX -= SCROLL_OFFSET;
        }

        if (newScrollX != lastScroll) {
            lastScroll = newScrollX;
            scrollTo(newScrollX, 0);
        }
    }

    /**
     * Refresh tabs
     * @param position of selected tab
     */
    public void refreshData(int position) {
        mLinearLayout.removeAllViews();
        for (int i = 0; i < mViewPager.getAdapter().getCount(); i++) {
            TabPagerAdapter tabPagerAdapter = (TabPagerAdapter) mViewPager.getAdapter();
            View view = tabPagerAdapter.getView(i, i == position);
            view.setFocusable(true);
            final int currentPosition = i;
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewPager.setCurrentItem(currentPosition);
                }
            });
            mLinearLayout.addView(view, currentPosition);
        }
    }

    protected class InternalPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            int offset = (int)positionOffset;
            if (mLinearLayout.getChildAt(position) != null) {
                offset = (int) (positionOffset * mLinearLayout.getChildAt(position).getWidth());
            }
            scrollView(position, offset);

            if (mDelegatePageChangeListener != null) {
                mDelegatePageChangeListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }
        }

        @Override
        public void onPageSelected(int position) {
            refreshData(position);

            if (mDelegatePageChangeListener != null) {
                mDelegatePageChangeListener.onPageSelected(position);
            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (state == ViewPager.SCROLL_STATE_IDLE) {
                scrollView(mViewPager.getCurrentItem(), 0);
            }

            if (mDelegatePageChangeListener != null) {
                mDelegatePageChangeListener.onPageScrollStateChanged(state);
            }

        }
    }

    /**
     * This interface must be implemented by the same class as PagerAdapter
     */
    public interface TabPagerAdapter {

        /**
         * Returns view for the tab
         * @param position position of the tab
         * @param isCurrent <code>true</code> if this tab is selected
         * @return
         */
        public View getView(int position, boolean isCurrent);
    }
}
