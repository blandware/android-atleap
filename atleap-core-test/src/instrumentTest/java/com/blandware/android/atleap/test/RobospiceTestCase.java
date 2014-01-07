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

import android.content.Intent;
import android.database.Cursor;
import android.test.AndroidTestCase;

import com.blandware.android.atleap.test.model.Book;
import com.blandware.android.atleap.test.provider.TestContract;
import com.blandware.android.atleap.test.robospice.BooksAuthorsRobospiceRequest;
import com.blandware.android.atleap.test.robospice.SetUrlInterface;
import com.blandware.android.atleap.test.robospice.TestRobospiceService;
import com.blandware.android.atleap.test.stub.RequestListenerStub;
import com.google.mockwebserver.MockResponse;
import com.google.mockwebserver.MockWebServer;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.request.SpiceRequest;

import org.apache.commons.io.IOUtils;

/**
 * Created by agrebnev on 29.12.13.
 */
public class RobospiceTestCase extends AndroidTestCase {

    private static final long SPICE_MANAGER_WAIT_TIMEOUT = 500;
    private static final String TEST_CACHE_KEY = "12345";
    private static final long TEST_DURATION = DurationInMillis.ALWAYS_EXPIRED;
    private static final long REQUEST_COMPLETION_TIME_OUT = 1000 * 60;

    private static final String BOOKS_FILENAME = "books_authors.json";

    private static final String[] BOOKS_AUTHORS_PROJECTION = new String[] {
            TestContract.Book._ID,
            TestContract.Book.TITLE,
            TestContract.Book.PUBLISHING_DATE,
            TestContract.Book.TAGS_STRING,
            TestContract.Book.PUBLISHER_TITLE,
            TestContract.Book.PUBLISHER_URL,
            TestContract.BookAuthor.AUTHOR_ID
            };

    private SpiceManager spiceManager;
    private MockWebServer mockWebServer;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        spiceManager = new SpiceManager(TestRobospiceService.class);
        mockWebServer = new MockWebServer();
    }

    @Override
    protected void tearDown() throws Exception {
        waitForSpiceManagerShutdown(spiceManager);
        getContext().stopService(new Intent(getContext(), TestRobospiceService.class));
        mockWebServer.shutdown();
        super.tearDown();
    }

    private void waitForSpiceManagerShutdown(SpiceManager spiceManager) throws InterruptedException {
        if (spiceManager != null && spiceManager.isStarted()) {
            spiceManager.cancelAllRequests();
            spiceManager.removeAllDataFromCache();
            spiceManager.shouldStopAndJoin(SPICE_MANAGER_WAIT_TIMEOUT);
            spiceManager = null;
        }
    }

    public void test_books_authors_tables_sql() throws Exception {
        Book.BooksResult result = makeRobospiceRequest(BOOKS_FILENAME, new BooksAuthorsRobospiceRequest());

        assertTrue(result.resultCode == 100);
        assertTrue(result.getBooks() != null);
        assertTrue(result.getBooks().size() == 2);


        assertTrue(result.getBooks().toArray(new Book[0])[0].getAuthors() != null);
        assertTrue(result.getBooks().toArray(new Book[0])[0].getAuthors().size() == 2);

        assertTrue(result.getBooks().toArray(new Book[0])[0].getTags() != null);
        assertTrue(result.getBooks().toArray(new Book[0])[0].getTags().size() == 2);

        assertTrue(result.getBooks().toArray(new Book[0])[0].getPublisherTitle() != null);
        assertTrue(result.getBooks().toArray(new Book[0])[0].getPublisherUrl() != null);


        assertTrue(result.getBooks().toArray(new Book[0])[1].getAuthors() != null);
        assertTrue(result.getBooks().toArray(new Book[0])[1].getAuthors().size() == 2);

        assertTrue(result.getBooks().toArray(new Book[0])[1].getTags() != null);
        assertTrue(result.getBooks().toArray(new Book[0])[1].getTags().size() == 2);

        assertTrue(result.getBooks().toArray(new Book[0])[1].getPublisherTitle() != null);
        assertTrue(result.getBooks().toArray(new Book[0])[1].getPublisherUrl() != null);



        Cursor cursor = getContext().getContentResolver().query(
                TestContract.CONTENT_URI_BOOKS_AUTHORS1, //uri
                BOOKS_AUTHORS_PROJECTION, //projection
                null, //selection
                null, //selectionArgs
                null //sort order
        );

        assertTrue(cursor != null);
        assertTrue(cursor.getCount() == 4);
    }

    public void test_books_authors_raw_sql() throws Exception {
        Book.BooksResult result = makeRobospiceRequest(BOOKS_FILENAME, new BooksAuthorsRobospiceRequest());

        Cursor cursor = getContext().getContentResolver().query(
                TestContract.CONTENT_URI_BOOKS_AUTHORS2, //uri
                BOOKS_AUTHORS_PROJECTION, //projection
                TestContract.BookAuthor.AUTHOR_ID + "=?", //selection
                new String[]{"sashab"}, //selectionArgs
                TestContract.Book.TITLE + " ASC" //sort order
        );

        assertTrue(cursor != null);
        assertTrue(cursor.getCount() == 2);
    }

    public void test_books_authors_callback_sql() throws Exception {
        Book.BooksResult result = makeRobospiceRequest(BOOKS_FILENAME, new BooksAuthorsRobospiceRequest());

        Cursor cursor = getContext().getContentResolver().query(
                TestContract.CONTENT_URI_BOOKS_AUTHORS3, //uri
                BOOKS_AUTHORS_PROJECTION, //projection
                TestContract.BookAuthor.AUTHOR_ID + "=?", //selection
                new String[]{"sashab"}, //selectionArgs
                TestContract.Book.TITLE + " ASC" //sort order
        );

        assertTrue(cursor != null);
        assertTrue(cursor.getCount() == 2);
    }


    public void test_book_by_id() throws Exception {
        Book.BooksResult result = makeRobospiceRequest(BOOKS_FILENAME, new BooksAuthorsRobospiceRequest());

        Cursor cursor = getContext().getContentResolver().query(
                TestContract.Book.CONTENT_URI.buildUpon().appendPath("1382763").build(), //uri
                null, //projection
                null, //selection
                null, //selectionArgs
                null //sort order
        );

        assertTrue(cursor != null);
        assertTrue(cursor.getCount() == 1);

        cursor.moveToFirst();

        assertTrue(cursor.getString(cursor.getColumnIndex(TestContract.Book.TITLE)) != null);
        assertTrue(cursor.getString(cursor.getColumnIndex(TestContract.Book.TITLE)).equals("Super book"));
    }

    public void test_book_by_id_with_args() throws Exception {
        Book.BooksResult result = makeRobospiceRequest(BOOKS_FILENAME, new BooksAuthorsRobospiceRequest());

        Cursor cursor = getContext().getContentResolver().query(
                TestContract.Book.CONTENT_URI.buildUpon().appendPath("1382763").build(), //uri
                null, //projection
                TestContract.Book.PUBLISHER_TITLE + "=?", //selection
                new String[]{"Wow publisher"}, //selectionArgs
                null //sort order
        );

        assertTrue(cursor != null);
        assertTrue(cursor.getCount() == 1);

        cursor.moveToFirst();

        assertTrue(cursor.getString(cursor.getColumnIndex(TestContract.Book.TITLE)) != null);
        assertTrue(cursor.getString(cursor.getColumnIndex(TestContract.Book.TITLE)).equals("Super book"));
    }


    protected <T> T makeRobospiceRequest(String fileName, SpiceRequest<T> spiceRequest) throws Exception {
        String jsonString = IOUtils.toString(getContext().getResources().getAssets().open(fileName));
        mockWebServer.enqueue(new MockResponse().setBody(jsonString));
        mockWebServer.play();

        // when
        spiceManager.start(getContext());

        String url = mockWebServer.getUrl("/").toString();

        SetUrlInterface request = (SetUrlInterface) spiceRequest;
        request.setUrl(url);


        RequestListenerStub<T> requestListenerStub = new RequestListenerStub<T>();

        // when
        spiceManager.execute(spiceRequest, TEST_CACHE_KEY, TEST_DURATION, requestListenerStub);
        requestListenerStub.await(REQUEST_COMPLETION_TIME_OUT);

        // com.blandware.android.atleap.test
        assertTrue(requestListenerStub.isSuccessful());
        assertTrue(requestListenerStub.isExecutedInUIThread());
        assertTrue(requestListenerStub.getResultHistory() != null);
        assertTrue(requestListenerStub.getResultHistory().size()> 0);


        return requestListenerStub.getResultHistory().get(0);

    }

}
