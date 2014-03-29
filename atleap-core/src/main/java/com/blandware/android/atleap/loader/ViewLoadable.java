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

package com.blandware.android.atleap.loader;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * This class represent View to load data using LoaderManager and CursorLoader
 */
public class ViewLoadable implements LoaderManager.LoaderCallbacks<Cursor> {

    protected Uri mUri;
    protected String[] mProjection;
    protected String mSelection;
    protected String[] mSelectionArgs;
    protected String mSortOrder;
    protected Context mContext;

    protected Cursor mCursor;

    /**
     * A list of columns containing the data to bind to the UI.
     * This field should be made private, so it is hidden from the SDK.
     */
    protected int[] mFrom;

    /**
     * A list of View ids representing the views to which the data must be bound.
     * This field should be made private, so it is hidden from the SDK.
     */
    protected int[] mTo;


    protected ViewBinder mViewBinder;

    protected String[] mOriginalFrom;

    protected View mView;

    /**
     * @param context The context where the ListView associated with this
     *            SimpleListItemFactory is running
     *
     * @param uri The URI, using the content:// scheme, for the content to
     *         retrieve.
     * @param view The view should include at least
     *            those named views defined in "to"
     */
    public ViewLoadable(
            Context context,
            Uri uri,
            View view
    ) {
        this(context, uri, null, null, null, null, view, null, null, null);
    }

    /**
     * @param context The context where the ListView associated with this
     *            SimpleListItemFactory is running
     *
     * @param uri The URI, using the content:// scheme, for the content to
     *         retrieve.
     * @param view The view should include at least
     *            those named views defined in "to"
     * @param fromFieldNames A list of column names representing the data to bind to the UI.  Can be null
     *            if the cursor is not available yet.
     * @param toLayoutViewIds The views that should display column in the "from" parameter.
     *            These should all be TextViews. The first N views in this list
     *            are given the values of the first N columns in the from
     *            parameter.  Can be null if the cursor is not available yet.
     */
    public ViewLoadable(
            Context context,
            Uri uri,
            View view,
            String[] fromFieldNames,
            int[] toLayoutViewIds
    ) {
        this(context, uri, null, null, null, null, view, null, fromFieldNames, toLayoutViewIds);
    }

    /**
     * @param context The context where the ListView associated with this
     *            SimpleListItemFactory is running
     *
     * @param uri The URI, using the content:// scheme, for the content to
     *         retrieve.
     * @param projection A list of which columns to return. Passing null will
     *         return all columns, which is inefficient.
     * @param selection A filter declaring which rows to return, formatted as an
     *         SQL WHERE clause (excluding the WHERE itself). Passing null will
     *         return all rows for the given URI.
     * @param selectionArgs You may include ?s in selection, which will be
     *         replaced by the values from selectionArgs, in the order that they
     *         appear in the selection. The values will be bound as Strings.
     * @param sortOrder How to order the rows, formatted as an SQL ORDER BY
     *         clause (excluding the ORDER BY itself). Passing null will use the
     *         default sort order, which may be unordered.
     * @param view The view should include at least
     *            those named views defined in "to"
     * @param fromFieldNames A list of column names representing the data to bind to the UI.  Can be null
     *            if the cursor is not available yet.
     * @param toLayoutViewIds The views that should display column in the "from" parameter.
     *            These should all be TextViews. The first N views in this list
     *            are given the values of the first N columns in the from
     *            parameter.  Can be null if the cursor is not available yet.
     */
    public ViewLoadable(
            Context context,
            Uri uri,
            String[] projection,
            String selection,
            String[] selectionArgs,
            String sortOrder,
            View view,
            String[] fromFieldNames,
            int[] toLayoutViewIds
    ) {
        this(context, uri, projection, selection, selectionArgs, sortOrder, view, null, fromFieldNames, toLayoutViewIds);
    }

    /**
     * @param context The context where the ListView associated with this
     *            SimpleListItemFactory is running
     *
     * @param uri The URI, using the content:// scheme, for the content to
     *         retrieve.
     * @param projection A list of which columns to return. Passing null will
     *         return all columns, which is inefficient.
     * @param selection A filter declaring which rows to return, formatted as an
     *         SQL WHERE clause (excluding the WHERE itself). Passing null will
     *         return all rows for the given URI.
     * @param selectionArgs You may include ?s in selection, which will be
     *         replaced by the values from selectionArgs, in the order that they
     *         appear in the selection. The values will be bound as Strings.
     * @param sortOrder How to order the rows, formatted as an SQL ORDER BY
     *         clause (excluding the ORDER BY itself). Passing null will use the
     *         default sort order, which may be unordered.
     * @param view The view should include at least
     *            those named views defined in "to"
     * @param cursor The database cursor.  Can be null if the cursor is not available yet.
     * @param fromFieldNames A list of column names representing the data to bind to the UI.  Can be null
     *            if the cursor is not available yet.
     * @param toLayoutViewIds The views that should display column in the "from" parameter.
     *            These should all be TextViews. The first N views in this list
     *            are given the values of the first N columns in the from
     *            parameter.  Can be null if the cursor is not available yet.
     */
    public ViewLoadable(
            Context context,
            Uri uri,
            String[] projection,
            String selection,
            String[] selectionArgs,
            String sortOrder,
            View view,
            Cursor cursor,
            String[] fromFieldNames,
            int[] toLayoutViewIds
    ) {
        mContext = context;
        mUri = uri;
        mProjection = projection;
        mSelection = selection;
        mSelectionArgs = selectionArgs;
        mSortOrder = sortOrder;
        mView = view;
        mCursor = cursor;
        mTo = toLayoutViewIds;
        mOriginalFrom = fromFieldNames;

        findColumns(fromFieldNames);

    }



    /**
     * Create a map from an array of strings to an array of column-id integers in mCursor.
     * If mCursor is null, the array will be discarded.
     *
     * @param from the Strings naming the columns of interest
     */
    protected void findColumns(String[] from) {
        if (mCursor != null) {
            int i;
            int count = from.length;
            if (mFrom == null || mFrom.length != count) {
                mFrom = new int[count];
            }
            for (i = 0; i < count; i++) {
                mFrom[i] = mCursor.getColumnIndexOrThrow(from[i]);
            }
        } else {
            mFrom = null;
        }
    }



    /**
     * Change the cursor and change the column-to-view mappings at the same time.
     *
     * @param c The database cursor.  Can be null if the cursor is not available yet.
     * @param from A list of column names representing the data to bind to the UI.  Can be null
     *            if the cursor is not available yet.
     * @param to The views that should display column in the "from" parameter.
     *            These should all be TextViews. The first N views in this list
     *            are given the values of the first N columns in the from
     *            parameter.  Can be null if the cursor is not available yet.
     */
    public void changeCursorAndColumns(Cursor c, String[] from, int[] to) {
        mOriginalFrom = from;
        mTo = to;
        changeCursor(c);
    }

    /**
     * Change the underlying cursor to a new cursor. If there is an existing cursor it will be
     * closed.
     *
     * @param cursor The new cursor to be used
     */
    public void changeCursor(Cursor cursor) {
        Cursor old = swapCursor(cursor);
        if (old != null) {
            old.close();
        }
    }

    /**
     * Swap in a new Cursor, returning the old Cursor.  Unlike
     * {@link #changeCursor(Cursor)}, the returned old Cursor is <em>not</em>
     * closed.
     *
     * @param newCursor The new cursor to be used.
     * @return Returns the previously set Cursor, or null if there wasa not one.
     * If the given new Cursor is the same instance is the previously set
     * Cursor, null is also returned.
     */
    public Cursor swapCursor(Cursor newCursor) {
        if (newCursor == mCursor) {
            return null;
        }
        Cursor oldCursor = mCursor;
        mCursor = newCursor;

        findColumns(mOriginalFrom);
        bindView(mCursor);
        return oldCursor;
    }


    /**
     * Binds all of the field names passed into the "to" parameter of the
     * constructor with their corresponding cursor columns as specified in the
     * "from" parameter.
     *
     * Binding occurs in two phases. First, if a
     * {@link com.blandware.android.atleap.loader.ViewLoadable.ViewBinder} is available,
     * {@link ViewBinder#setViewValue(android.view.View, android.database.Cursor, int)}
     * is invoked. If the returned value is true, binding has occured. If the
     * returned value is false and the view to bind is a TextView,
     * {@link #setViewText(android.widget.TextView, String)} is invoked. If the returned value is
     * false and the view to bind is an ImageView,
     * {@link #setViewImage(android.widget.ImageView, String)} is invoked. If no appropriate
     * binding can be found, an {@link IllegalStateException} is thrown.
     *
     * @throws IllegalStateException if binding cannot occur
     *
     * @see com.blandware.android.atleap.loader.ViewLoadable#bindView(android.database.Cursor)
     * @see #getViewBinder()
     * @see #setViewBinder(com.blandware.android.atleap.loader.ViewLoadable.ViewBinder)
     * @see #setViewImage(android.widget.ImageView, String)
     * @see #setViewText(android.widget.TextView, String)
     */
    public void bindView(Cursor cursor) {
        if (cursor == null || cursor.getCount() == 0)
            return;

        cursor.moveToFirst();

        final ViewBinder binder = mViewBinder;
        final int count = mTo.length;
        final int[] from = mFrom;
        final int[] to = mTo;

        for (int i = 0; i < count; i++) {
            final View v = mView.findViewById(to[i]);
            if (v != null) {
                boolean bound = false;
                if (binder != null) {
                    bound = binder.setViewValue(v, cursor, from[i]);
                }

                if (!bound) {
                    String text = cursor.getString(from[i]);
                    if (text == null) {
                        text = "";
                    }

                    if (v instanceof TextView) {
                        setViewText((TextView) v, text);
                    } else if (v instanceof ImageView) {
                        setViewImage((ImageView) v, text);
                    } else {
                        throw new IllegalStateException(v.getClass().getName() + " is not a " +
                                " view that can be bounds by this SimpleCursorAdapter");
                    }
                }
            }
        }
    }

    /**
     * Returns the {@link ViewBinder} used to bind data to views.
     *
     * @return a ViewBinder or null if the binder does not exist
     *
     * @see #bindView(android.database.Cursor)
     * @see #setViewBinder(com.blandware.android.atleap.loader.ViewLoadable.ViewBinder)
     */
    public ViewBinder getViewBinder() {
        return mViewBinder;
    }

    /**
     * Sets the binder used to bind data to views.
     *
     * @param viewBinder the binder used to bind data to views, can be null to
     *        remove the existing binder
     *
     * @see #bindView(android.database.Cursor)
     * @see #getViewBinder()
     */
    public void setViewBinder(ViewBinder viewBinder) {
        mViewBinder = viewBinder;
    }

    /**
     * Called by bindView() to set the image for an ImageView but only if
     * there is no existing ViewBinder or if the existing ViewBinder cannot
     * handle binding to an ImageView.
     *
     * By default, the value will be treated as an image resource. If the
     * value cannot be used as an image resource, the value is used as an
     * image Uri.
     *
     * Intended to be overridden that need to filter strings
     * retrieved from the database.
     *
     * @param v ImageView to receive an image
     * @param value the value retrieved from the cursor
     */
    public void setViewImage(ImageView v, String value) {
        try {
            v.setImageResource(Integer.parseInt(value));
        } catch (NumberFormatException nfe) {
            v.setImageURI(Uri.parse(value));
        }
    }

    /**
     * Called by bindView() to set the text for a TextView but only if
     * there is no existing ViewBinder or if the existing ViewBinder cannot
     * handle binding to an TextView.
     *
     * Intended to be overridden that need to filter strings
     * retrieved from the database.
     *
     * @param v TextView to receive text
     * @param text the text to be set for the TextView
     */
    public void setViewText(TextView v, String text) {
        v.setText(text);
    }


    /**
     * Instantiate and return a new Loader for the given ID.
     *
     * @param id The ID whose loader is to be created.
     * @param args Any arguments supplied by the caller.
     * @return Return a new Loader instance that is ready to start loading.
     */
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(mContext,
                mUri,
                mProjection,
                mSelection,
                mSelectionArgs,
                mSortOrder
        );
    }


    /**
     * Called when a previously created loader has finished its load.  Note
     * that normally an application is <em>not</em> allowed to commit fragment
     * transactions while in this call, since it can happen after an
     * activity's state is saved.  See {@link android.support.v4.app.FragmentManager#beginTransaction()
     * FragmentManager.openTransaction()} for further discussion on this.
     *
     * <p>This function is guaranteed to be called prior to the release of
     * the last data that was supplied for this Loader.  At this point
     * you should remove all use of the old data (since it will be released
     * soon), but should not do your own release of the data since its Loader
     * owns it and will take care of that.  The Loader will take care of
     * management of its data so you don't have to.  In particular:
     *
     * <ul>
     * <li> <p>The Loader will monitor for changes to the data, and report
     * them to you through new calls here.  You should not monitor the
     * data yourself.  For example, if the data is a {@link android.database.Cursor}
     * and you place it in a {@link android.widget.CursorAdapter}, use
     * the {@link android.widget.CursorAdapter#CursorAdapter(android.content.Context,
     * android.database.Cursor, int)} constructor <em>without</em> passing
     * in either {@link android.widget.CursorAdapter#FLAG_AUTO_REQUERY}
     * or {@link android.widget.CursorAdapter#FLAG_REGISTER_CONTENT_OBSERVER}
     * (that is, use 0 for the flags argument).  This prevents the CursorAdapter
     * from doing its own observing of the Cursor, which is not needed since
     * when a change happens you will get a new Cursor throw another call
     * here.
     * <li> The Loader will release the data once it knows the application
     * is no longer using it.  For example, if the data is
     * a {@link android.database.Cursor} from a {@link android.content.CursorLoader},
     * you should not call close() on it yourself.  If the Cursor is being placed in a
     * {@link android.widget.CursorAdapter}, you should use the
     * {@link android.widget.CursorAdapter#swapCursor(android.database.Cursor)}
     * method so that the old Cursor is not closed.
     * </ul>
     *
     * @param loader The Loader that has finished.
     * @param data The data generated by the Loader.
     */
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (mTo == null) {
            changeCursorAndColumns(data, CursorUtil.getFieldNames(data), CursorUtil.getLayoutViewIds(mContext, CursorUtil.getFieldNames(data)));
        } else {
            swapCursor(data);
        }
    }

    /**
     * Called when a previously created loader is being reset, and thus
     * making its data unavailable.  The application should at this point
     * remove any references it has to the Loader's data.
     *
     * @param loader The Loader that is being reset.
     */
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        swapCursor(null);
    }

    /**
     * This class can be used by external clients of ViewLoadable
     * to bind values fom the Cursor to views.
     *
     * You should use this class to bind values from the Cursor to views
     * that are not directly supported by ViewLoadable or to
     * change the way binding occurs for views supported by
     * ViewLoadable.
     *
     * @see ViewLoadable#bindView(android.database.Cursor)
     * @see ViewLoadable#setViewImage(android.widget.ImageView, String)
     * @see ViewLoadable#setViewText(android.widget.TextView, String)
     */
    public static interface ViewBinder {
        /**
         * Binds the Cursor column defined by the specified index to the specified view.
         *
         * When binding is handled by this ViewBinder, this method must return true.
         * If this method returns false, SimpleCursorAdapter will attempts to handle
         * the binding on its own.
         *
         * @param view the view to bind the data to
         * @param cursor the cursor to get the data from
         * @param columnIndex the column at which the data can be found in the cursor
         *
         * @return true if the data was bound to the view, false otherwise
         */
        boolean setViewValue(View view, Cursor cursor, int columnIndex);
    }


}
