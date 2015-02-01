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

/**
 * This SimpleCursorAdapter implements LoaderManager.LoaderCallbacks<Cursor>
 */
public class SimpleCursorRecyclerAdapterLoadable extends SimpleCursorRecyclerAdapter implements LoaderManager.LoaderCallbacks<Cursor> {

    protected Uri mUri;
    protected String[] mProjection;
    protected String mSelection;
    protected String[] mSelectionArgs;
    protected String mSortOrder;

    /**
     * @param context The context where the ListView associated with this
     *            SimpleListItemFactory is running
     *
     * @param uri The URI, using the content:// scheme, for the content to
     *         retrieve.
     * @param layoutId resource identifier of a layout file that defines the views
     *            for this list item. The layout file should include at least
     *            those named views defined in "to"
     */
    public SimpleCursorRecyclerAdapterLoadable(
            Context context,
            Uri uri,
            int layoutId
    ) {
        this(context, uri, null, null, null, null, layoutId, null, null, null, 0);
    }

    /**
     * @param context The context where the ListView associated with this
     *            SimpleListItemFactory is running
     *
     * @param uri The URI, using the content:// scheme, for the content to
     *         retrieve.
     * @param layoutId resource identifier of a layout file that defines the views
     *            for this list item. The layout file should include at least
     *            those named views defined in "to"
     * @param fromFieldNames A list of column names representing the data to bind to the UI.  Can be null
     *            if the cursor is not available yet.
     * @param toLayoutViewIds The views that should display column in the "from" parameter.
     *            These should all be TextViews. The first N views in this list
     *            are given the values of the first N columns in the from
     *            parameter.  Can be null if the cursor is not available yet.
     */
    public SimpleCursorRecyclerAdapterLoadable(
            Context context,
            Uri uri,
            int layoutId,
            String[] fromFieldNames,
            int[] toLayoutViewIds
    ) {
        this(context, uri, null, null, null, null, layoutId, null, fromFieldNames, toLayoutViewIds, 0);
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
     * @param layoutId resource identifier of a layout file that defines the views
     *            for this list item. The layout file should include at least
     *            those named views defined in "to"
     * @param fromFieldNames A list of column names representing the data to bind to the UI.  Can be null
     *            if the cursor is not available yet.
     * @param toLayoutViewIds The views that should display column in the "from" parameter.
     *            These should all be TextViews. The first N views in this list
     *            are given the values of the first N columns in the from
     *            parameter.  Can be null if the cursor is not available yet.
     */
    public SimpleCursorRecyclerAdapterLoadable(
            Context context,
            Uri uri,
            String[] projection,
            String selection,
            String[] selectionArgs,
            String sortOrder,
            int layoutId,
            String[] fromFieldNames,
            int[] toLayoutViewIds
    ) {
        this(context, uri, projection, selection, selectionArgs, sortOrder, layoutId, null, fromFieldNames, toLayoutViewIds, 0);
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
     * @param layoutId resource identifier of a layout file that defines the views
     *            for this list item. The layout file should include at least
     *            those named views defined in "to"
     * @param cursor The database cursor.  Can be null if the cursor is not available yet.
     * @param fromFieldNames A list of column names representing the data to bind to the UI.  Can be null
     *            if the cursor is not available yet.
     * @param toLayoutViewIds The views that should display column in the "from" parameter.
     *            These should all be TextViews. The first N views in this list
     *            are given the values of the first N columns in the from
     *            parameter.  Can be null if the cursor is not available yet.
     * @param flags Flags used to determine the behavior of the adapter,
     * as per {@link android.support.v4.widget.CursorAdapter#CursorAdapter(android.content.Context, android.database.Cursor, int)}.
     */
    public SimpleCursorRecyclerAdapterLoadable(
            Context context,
            Uri uri,
            String[] projection,
            String selection,
            String[] selectionArgs,
            String sortOrder,
            int layoutId,
            Cursor cursor,
            String[] fromFieldNames,
            int[] toLayoutViewIds,
            int flags
    ) {
        super(context, layoutId, cursor, fromFieldNames, toLayoutViewIds, flags);
        mUri = uri;
        mProjection = projection;
        mSelection = selection;
        mSelectionArgs = selectionArgs;
        mSortOrder = sortOrder;
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


}
