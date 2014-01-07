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

package com.blandware.android.atleap.test;

import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.test.AndroidTestCase;

import com.blandware.android.atleap.test.provider.TestContract;

import java.util.ArrayList;

/**
 * Created by agrebnev on 06.01.14.
 */
public class CRUDTestCase extends AndroidTestCase {

    private static final String USER_NAME1 = "romar";
    private static final String FULL_NAME1 = "Roman Romanov";

    private static final String USER_NAME2 = "vladv";
    private static final String FULL_NAME2 = "Vladimir Voron";

    @Override
    protected void setUp() throws Exception {
        getContext().getContentResolver().delete(TestContract.Author.CONTENT_URI, null, null);
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void test_author_crud() throws Exception {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TestContract.Author._ID, USER_NAME1);
        contentValues.put(TestContract.Author.FULL_NAME, FULL_NAME1);
        Uri uri = getContext().getContentResolver().insert(TestContract.Author.CONTENT_URI, contentValues);

        assertTrue(uri != null);
        assertTrue(uri.getLastPathSegment().equals(USER_NAME1));

        Cursor cursor = getContext().getContentResolver().query(
                uri, //uri
                null, //projection
                null, //selection
                null, //selectionArgs
                null //sort order
        );
        assertTrue(cursor != null);
        assertTrue(cursor.getCount() == 1);
        cursor.moveToFirst();
        assertTrue(cursor.getString(cursor.getColumnIndex(TestContract.Author._ID)) != null);
        assertTrue(cursor.getString(cursor.getColumnIndex(TestContract.Author._ID)).equals(USER_NAME1));


        contentValues = new ContentValues();
        contentValues.put(TestContract.Author._ID, USER_NAME2);
        contentValues.put(TestContract.Author.FULL_NAME, FULL_NAME2);
        int rowsCount = getContext().getContentResolver().update(
                TestContract.Author.CONTENT_URI.buildUpon().appendPath(USER_NAME1).build(),
                contentValues,
                null,
                null
                );

        assertTrue(rowsCount == 1);


        cursor = getContext().getContentResolver().query(
                TestContract.Author.CONTENT_URI.buildUpon().appendPath(USER_NAME2).build(), //uri
                null, //projection
                null, //selection
                null, //selectionArgs
                null //sort order
        );
        assertTrue(cursor != null);
        assertTrue(cursor.getCount() == 1);
        cursor.moveToFirst();
        assertTrue(cursor.getString(cursor.getColumnIndex(TestContract.Author._ID)) != null);
        assertTrue(cursor.getString(cursor.getColumnIndex(TestContract.Author._ID)).equals(USER_NAME2));

        getContext().getContentResolver().delete(
                TestContract.Author.CONTENT_URI.buildUpon().appendPath(USER_NAME2).build(),
                null,
                null
                );


        cursor = getContext().getContentResolver().query(
                TestContract.Author.CONTENT_URI.buildUpon().appendPath(USER_NAME2).build(), //uri
                null, //projection
                null, //selection
                null, //selectionArgs
                null //sort order
        );
        assertTrue(cursor != null);
        assertTrue(cursor.getCount() == 0);
    }


    public void test_author_bulk() throws Exception {
        ContentValues[] values = new ContentValues[2];
        ContentValues contentValues = new ContentValues();
        contentValues.put(TestContract.Author._ID, USER_NAME1);
        contentValues.put(TestContract.Author.FULL_NAME, FULL_NAME1);
        values[0] = contentValues;
        contentValues = new ContentValues();
        contentValues.put(TestContract.Author._ID, USER_NAME2);
        contentValues.put(TestContract.Author.FULL_NAME, FULL_NAME2);
        values[1] = contentValues;

        getContext().getContentResolver().bulkInsert(TestContract.Author.CONTENT_URI, values);

        Cursor cursor = getContext().getContentResolver().query(
                TestContract.Author.CONTENT_URI, //uri
                null, //projection
                null, //selection
                null, //selectionArgs
                null //sort order
        );
        assertTrue(cursor != null);
        assertTrue(cursor.getCount() == 2);

        ArrayList<ContentProviderOperation> operations = new ArrayList<ContentProviderOperation>(1);
        ContentProviderOperation operation = ContentProviderOperation.newDelete(TestContract.Author.CONTENT_URI).build();
        operations.add(operation);

        getContext().getContentResolver().applyBatch(TestContract.CONTENT_AUTHORITY, operations);

        cursor = getContext().getContentResolver().query(
                TestContract.Author.CONTENT_URI, //uri
                null, //projection
                null, //selection
                null, //selectionArgs
                null //sort order
        );
        assertTrue(cursor != null);
        assertTrue(cursor.getCount() == 0);
    }
}
