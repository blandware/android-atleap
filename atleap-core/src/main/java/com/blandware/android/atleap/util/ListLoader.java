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

package com.blandware.android.atleap.util;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.widget.ListView;

/**
 * This is the helper to load data to the {@link android.widget.ListView} using {@link android.support.v4.content.CursorLoader}
 */
public class ListLoader {


    private int mListViewId;
    private int mListLayoutId;
    private String[] mFromFieldNames;
    private int[] mToLayoutViewIds;
    private Uri mUri;
    private String[] mProjection;
    private String mSelection;
    private String[] mSelectionArgs;
    private String mSortOrder;

    private Context mContext;
    private int mLoaderId;
    private SimpleCursorAdapter mAdapter;
    private View mListView;


    /**
     * Create list loader
     * @param fragment fragment where the loader is attached
     * @param listViewId resource ID of the ListView
     * @param listLayoutId resource ID of the layout for the list item
     * @param uri uri of ContentProvider
     */
    public ListLoader(
            Fragment fragment,
            int listViewId,
            int listLayoutId,
            Uri uri
    ) {
        this(fragment, listViewId, listLayoutId, null, null, uri, null, null, null, null);
    }

    /**
     * Create list loader
     * @param fragment fragment where the loader is attached
     * @param listViewId resource ID of the ListView
     * @param listLayoutId resource ID of the layout for the list item
     * @param fromFieldNames array of the DB column names
     * @param toLayoutViewIds array of resource ID of the views in the list item layout
     * @param uri uri of ContentProvider
     */
    public ListLoader(
            Fragment fragment,
            int listViewId,
            int listLayoutId,
            String[] fromFieldNames,
            int[] toLayoutViewIds,
            Uri uri
    ) {
        this(fragment, listViewId, listLayoutId, fromFieldNames, toLayoutViewIds, uri, null, null, null, null);
    }

    /**
     * Create list loader
     * @param fragment fragment where the loader is attached
     * @param listViewId resource ID of the ListView
     * @param listLayoutId resource ID of the layout for the list item
     * @param fromFieldNames array of the DB column names
     * @param toLayoutViewIds array of resource ID of the views in the list item layout
     * @param uri uri of ContentProvider
     * @param projection array of the projection DB column names
     * @param selection selection string to DB query
     * @param selectionArgs array of selection arguments for the DB query
     * @param sortOrder sort order string for the DB query
     */
    public ListLoader(
            Fragment fragment,
            int listViewId,
            int listLayoutId,
            String[] fromFieldNames,
            int[] toLayoutViewIds,
            Uri uri,
            String[] projection,
            String selection,
            String[] selectionArgs,
            String sortOrder
    ) {

        mLoaderId = (int) HashUtil.longHash(((Object) fragment).getClass(), fragment.getArguments(), listViewId);
        mListView = fragment.getView().findViewById(listViewId);
        init(
                fragment.getActivity().getApplicationContext(),
                fragment.getLoaderManager(),
                listViewId,
                listLayoutId,
                fromFieldNames,
                toLayoutViewIds,
                uri,
                projection,
                selection,
                selectionArgs,
                sortOrder
        );
    }

    /**
     * Create list loader
     * @param activity activity where the loader is attached
     * @param listViewId resource ID of the ListView
     * @param listLayoutId resource ID of the layout for the list item
     * @param uri uri of ContentProvider
     */
    public ListLoader(
            FragmentActivity activity,
            int listViewId,
            int listLayoutId,
            Uri uri
    ) {
        this(activity, listViewId, listLayoutId, null, null, uri, null, null, null, null);
    }

    /**
     * Create list loader
     * @param activity activity where the loader is attached
     * @param listViewId resource ID of the ListView
     * @param listLayoutId resource ID of the layout for the list item
     * @param fromFieldNames array of the DB column names
     * @param toLayoutViewIds array of resource ID of the views in the list item layout
     * @param uri uri of ContentProvider
     */
    public ListLoader(
            FragmentActivity activity,
            int listViewId,
            int listLayoutId,
            String[] fromFieldNames,
            int[] toLayoutViewIds,
            Uri uri
    ) {
        this(activity, listViewId, listLayoutId, fromFieldNames, toLayoutViewIds, uri, null, null, null, null);
    }


    /**
     * Create list loader
     * @param activity activity where the loader is attached
     * @param listViewId resource ID of the ListView
     * @param listLayoutId resource ID of the layout for the list item
     * @param fromFieldNames array of the DB column names
     * @param toLayoutViewIds array of resource ID of the views in the list item layout
     * @param uri uri of ContentProvider
     * @param projection array of the projection DB column names
     * @param selection selection string to DB query
     * @param selectionArgs array of selection arguments for the DB query
     * @param sortOrder sort order string for the DB query
     */
    public ListLoader(
            FragmentActivity activity,
            int listViewId,
            int listLayoutId,
            String[] fromFieldNames,
            int[] toLayoutViewIds,
            Uri uri,
            String[] projection,
            String selection,
            String[] selectionArgs,
            String sortOrder
    ) {

        mLoaderId = (int) HashUtil.longHash(((Object) activity).getClass(), activity.getIntent(), listViewId);
        mListView = activity.findViewById(listViewId);
        init(
                activity.getApplicationContext(),
                activity.getSupportLoaderManager(),
                listViewId,
                listLayoutId,
                fromFieldNames,
                toLayoutViewIds,
                uri,
                projection,
                selection,
                selectionArgs,
                sortOrder
        );
    }

    /**
     * Create list loader
     * @param context context where the loader is attached
     * @param loaderManager loader manager of fragment or activity
     * @param listViewId resource ID of the ListView
     * @param listLayoutId resource ID of the layout for the list item
     * @param fromFieldNames array of the DB column names
     * @param toLayoutViewIds array of resource ID of the views in the list item layout
     * @param uri uri of ContentProvider
     * @param projection array of the projection DB column names
     * @param selection selection string to DB query
     * @param selectionArgs array of selection arguments for the DB query
     * @param sortOrder sort order string for the DB query
     */
    protected void init(
            Context context,
            LoaderManager loaderManager,
            int listViewId,
            int listLayoutId,
            String[] fromFieldNames,
            int[] toLayoutViewIds,
            Uri uri,
            String[] projection,
            String selection,
            String[] selectionArgs,
            String sortOrder
    ) {
        mContext = context;
        mListViewId = listViewId;
        mListLayoutId = listLayoutId;
        mFromFieldNames = fromFieldNames;
        mToLayoutViewIds = toLayoutViewIds;
        mUri = uri;
        mProjection = projection;
        mSelection = selection;
        mSelectionArgs = selectionArgs;
        mSortOrder = sortOrder;

        loaderManager.initLoader(mLoaderId, null, new LoaderCallback());
        mAdapter = new SimpleCursorAdapter(mContext, mListLayoutId, null, mFromFieldNames, mToLayoutViewIds, 0);

        if (mListView != null && mListView instanceof ListView) {
            ListView listView = (ListView)mListView;
            listView.setAdapter(mAdapter);
        }
    }

    /**
     * Get the list view adapter.
     * @return adapter
     */
    public SimpleCursorAdapter getAdapter() {
        return mAdapter;
    }

    /**
     * Loader callback
     */
    protected class LoaderCallback implements LoaderManager.LoaderCallbacks<Cursor> {

        /**
         * {@inheritDoc}
         */
        @Override
        public Loader onCreateLoader(int i, Bundle bundle) {
            return new CursorLoader(mContext,
                    mUri,
                    mProjection,
                    mSelection,
                    mSelectionArgs,
                    mSortOrder
            );
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
            if (mFromFieldNames == null || mToLayoutViewIds == null) {
                mFromFieldNames = getFieldNames(cursor);
                mToLayoutViewIds = getLayoutViewIds(mFromFieldNames);
                mAdapter.changeCursorAndColumns(cursor, mFromFieldNames, mToLayoutViewIds);
            } else {
                mAdapter.swapCursor(cursor);
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
            if (mAdapter != null)
                mAdapter.swapCursor(null);
        }
    }


    /**
     * Get table column names from the cursor
     * @param cursor
     * @return array of the field names
     */
    protected String[] getFieldNames(Cursor cursor) {
        if (cursor == null)
            return null;

        return cursor.getColumnNames();
    }

    /**
     * Find the resource id for the views located on the layout
     * @param fieldNames column names of the DB table
     * @return array of the layout resource id
     */
    protected int[] getLayoutViewIds(String[] fieldNames) {
        if (fieldNames == null || fieldNames.length == 0)
            return null;

        int[] layoutViewIds = new int[fieldNames.length];
        for (int i = 0; i < fieldNames.length; i++) {
            String fieldName = fieldNames[i];
            int id = mContext.getResources().getIdentifier(fieldName, "id", mContext.getPackageName());
            layoutViewIds[i] = id;
        }

        return layoutViewIds;
    }

}
