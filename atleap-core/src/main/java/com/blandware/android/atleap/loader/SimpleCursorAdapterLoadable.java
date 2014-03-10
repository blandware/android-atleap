package com.blandware.android.atleap.loader;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;

/**
 * Created by agrebnev on 10.03.14.
 */
public class SimpleCursorAdapterLoadable extends SimpleCursorAdapter implements LoaderManager.LoaderCallbacks<Cursor> {

    protected Uri mUri;
    protected String[] mProjection;
    protected String mSelection;
    protected String[] mSelectionArgs;
    protected String mSortOrder;

    public SimpleCursorAdapterLoadable(
            Context context,
            Uri uri,
            int layoutId
    ) {
        this(context, uri, null, null, null, null, layoutId, null, null, null, 0);
    }

    public SimpleCursorAdapterLoadable(
            Context context,
            Uri uri,
            int layoutId,
            String[] fromFieldNames,
            int[] toLayoutViewIds
    ) {
        this(context, uri, null, null, null, null, layoutId, null, fromFieldNames, toLayoutViewIds, 0);
    }

    public SimpleCursorAdapterLoadable(
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

    public SimpleCursorAdapterLoadable(
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

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (mTo == null) {
            changeCursorAndColumns(data, CursorUtil.getFieldNames(data), CursorUtil.getLayoutViewIds(mContext, CursorUtil.getFieldNames(data)));
        } else {
            swapCursor(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        swapCursor(null);
    }


}
