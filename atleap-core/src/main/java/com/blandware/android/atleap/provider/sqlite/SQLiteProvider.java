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

package com.blandware.android.atleap.provider.sqlite;

import android.content.ContentProvider;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.BaseColumns;
import android.provider.ContactsContract;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Locale;

/**
 * This class extend the ContentProvider to work with SQLiteDatabase
 */
public abstract class SQLiteProvider<H extends SQLiteOpenHelper, U extends SQLiteUriMatcher> extends ContentProvider {

    private static final String TAG = SQLiteProvider.class.getSimpleName();

    protected U mUriMatcher;

    protected H mDatabaseHelper = null;

    protected Uri mBaseContentUri;

    public H getDatabaseHelper() {
        return this.mDatabaseHelper;
    }

    protected abstract H createHelper();

    public abstract U getUriMatcher();

    public abstract String getAuthority();

    /**
     * {@inheritDoc}
     *
     */
    @Override
    public boolean onCreate() {
        mDatabaseHelper = createHelper();
        mUriMatcher = getUriMatcher();
        mBaseContentUri = new Uri.Builder().scheme(ContentResolver.SCHEME_CONTENT).authority(getAuthority()).build();
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getType(Uri uri) {
        return mUriMatcher.getType(uri);
    }

    /** {@inheritDoc} */
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        final SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();
        SQLiteMatcherEntry entry = mUriMatcher.getMatcherEntry(uri);

        Cursor cursor;
        if (!TextUtils.isEmpty(entry.getTablesSQL())) {
            if (entry.getBaseType().equals(SQLiteMatcherEntry.Type.ITEM) && (entry.getPath().endsWith("*") || entry.getPath().endsWith("#"))) {
                String id = getId(uri);
                String idFieldName = getIdFieldName(entry);
                String newSelection = addSelection(selection, idFieldName + "=?");
                String[] newSelectionArgs = addSelectionArg(selectionArgs, id);
                cursor = db.query(entry.getTablesSQL(), projection, newSelection, newSelectionArgs, null, null, sortOrder);
            } else {
                cursor = db.query(entry.getTablesSQL(), projection, selection, selectionArgs, null, null, sortOrder);
            }

        } else if (!TextUtils.isEmpty(entry.getRawSQL())) {
            String rawSQL = entry.getRawSQL();
            if (entry.getRawSQL().contains("%s")) {
                rawSQL = buildSQL(entry.getRawSQL(), projection, selection, sortOrder);
            }
            cursor =  db.rawQuery(rawSQL, selectionArgs);
        } else if (entry.getCallback() != null) {
            String rawSQL = entry.getCallback().getRawSQL(uri, projection, selection, selectionArgs, sortOrder);
            cursor =  db.rawQuery(rawSQL, selectionArgs);
        } else {
            throw new IllegalStateException("MatcherEntry does not contain SQL");
        }

        if (cursor != null) {
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
        }
        return cursor;
    }

    protected String getId(Uri uri) {
        return uri.getLastPathSegment();
    }


    /** {@inheritDoc} */
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        SQLiteMatcherEntry entry = mUriMatcher.getMatcherEntry(uri);

        long rowId = db.insertOrThrow(entry.getTablesSQL(), null, values);
        if (rowId == -1)
            return null;
        boolean syncToNetwork = !hasCallerIsSyncAdapterParameter(uri);
        getContext().getContentResolver().notifyChange(uri, null, syncToNetwork);
        String idFieldName = getIdFieldName(entry);
        String idValue = values.getAsString(idFieldName);
        if (TextUtils.isEmpty(idValue))
            idValue = String.valueOf(rowId);
        return mBaseContentUri.buildUpon().appendPath(entry.getPath()).appendPath(idValue).build();
    }

    /** {@inheritDoc} */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        SQLiteMatcherEntry entry = mUriMatcher.getMatcherEntry(uri);

        int retVal;
        if (entry.getBaseType().equals(SQLiteMatcherEntry.Type.ITEM) && (entry.getPath().endsWith("*") || entry.getPath().endsWith("#"))) {
            String id = getId(uri);
            String idFieldName = getIdFieldName(entry);
            String newSelection = addSelection(selection, idFieldName + "=?");
            String[] newSelectionArgs = addSelectionArg(selectionArgs, id);
            retVal = db.delete(entry.getTablesSQL(), newSelection, newSelectionArgs);
        } else {
            retVal = db.delete(entry.getTablesSQL(), selection, selectionArgs);
        }
        boolean syncToNetwork = !hasCallerIsSyncAdapterParameter(uri);
        getContext().getContentResolver().notifyChange(uri, null, syncToNetwork);
        return retVal;
    }

    /** {@inheritDoc} */
    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        SQLiteMatcherEntry entry = mUriMatcher.getMatcherEntry(uri);

        int retVal;
        if (entry.getBaseType().equals(SQLiteMatcherEntry.Type.ITEM) && (entry.getPath().endsWith("*") || entry.getPath().endsWith("#"))) {
            String id = getId(uri);
            String idFieldName = getIdFieldName(entry);
            String newSelection = addSelection(selection, idFieldName + "=?");
            String[] newSelectionArgs = addSelectionArg(selectionArgs, id);
            retVal = db.update(entry.getTablesSQL(), values, newSelection, newSelectionArgs);
        } else {
            retVal = db.update(entry.getTablesSQL(), values, selection, selectionArgs);
        }
        boolean syncToNetwork = !hasCallerIsSyncAdapterParameter(uri);
        getContext().getContentResolver().notifyChange(uri, null, syncToNetwork);
        return retVal;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        int result = 0;
        SQLiteDatabase db = this.getDatabaseHelper().getWritableDatabase();
        db.beginTransaction();
        try {
            for (ContentValues value : values) {

                Uri resultBulkInsert = this.insert(uri, value);
                if (resultBulkInsert != null) {
                    result++;
                }
            }
            db.setTransactionSuccessful();
            this.getContext().getContentResolver().notifyChange(uri, null);
        } finally {
            db.endTransaction();
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ContentProviderResult[] applyBatch(ArrayList<ContentProviderOperation> operations) throws OperationApplicationException {
        ContentProviderResult[] result = null;

        SQLiteDatabase db = this.getDatabaseHelper().getWritableDatabase();

        db.beginTransaction();
        try {
            result = super.applyBatch(operations);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return result;
    }

    protected String buildSQL(String format, String[] projection, String selection,
                              String sortOrder) {
        StringBuilder projectionClause = new StringBuilder();

        if (projection != null && projection.length != 0) {
            SQLiteQueryBuilder.appendColumns(projectionClause, projection);
        } else {
            projectionClause.append("* ");
        }

        return String.format(Locale.getDefault(), format, projectionClause.toString(), selection, sortOrder);
    }

    protected String addSelection(String selection, String whereClause) {
        if (TextUtils.isEmpty(selection)) {
            return whereClause;
        } else {
            return new StringBuilder(selection).append(" AND ").append(whereClause).toString();
        }
    }

    protected String[] addSelectionArg(String[] selectionArgs, String selectionArg) {
        if (selectionArgs == null || selectionArgs.length == 0) {
            return new String[] {selectionArg};
        } else {
            String[] newSelectionArgs = new String[selectionArgs.length + 1];
            System.arraycopy(selectionArgs, 0, newSelectionArgs, 0, selectionArgs.length);
            newSelectionArgs[selectionArgs.length] = selectionArg;
            return newSelectionArgs;
        }
    }

    protected String getIdFieldName(SQLiteMatcherEntry entry) {
        return BaseColumns._ID;
    }


    /**
     * Mark the Uri if you are using ContentProvider from the SyncAdapter. It allows not to sync again.
     * @param uri uri to makr
     * @return marked uri
     */
    public static Uri addCallerIsSyncAdapterParameter(Uri uri) {
        return uri.buildUpon().appendQueryParameter(
                ContactsContract.CALLER_IS_SYNCADAPTER, "true").build();
    }

    /**
     * Check if the Uri is marked as called from SyncAdapter
     * @param uri uri for check
     * @return <code>true</code> if the caller is SyncAdapter
     */
    public static boolean hasCallerIsSyncAdapterParameter(Uri uri) {
        return TextUtils.equals("true",
                uri.getQueryParameter(ContactsContract.CALLER_IS_SYNCADAPTER));
    }


}
