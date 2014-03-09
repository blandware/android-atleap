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

package com.blandware.android.atleap.test.provider;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by agrebnev on 01.01.14.
 */
public class TestContract {

    private TestContract() {}

    public static final String CONTENT_AUTHORITY = "com.blandware.android.atleap.test.authority";

    public static final Uri BASE_CONTENT_URI = new Uri.Builder().scheme(ContentResolver.SCHEME_CONTENT).authority(CONTENT_AUTHORITY).build();


    public static final String PATH_BOOKS = "books";
    public static final String PATH_BOOK = "books/#";

    public static class Book implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_BOOKS).build();
        public static final String TABLE = "book";
        public static final String TITLE = "title";
        public static final String PUBLISHING_DATE = "publishing_date";
        public static final String TAGS_STRING = "tags_string";
        public static final String PUBLISHER_TITLE = "publisher_title";
        public static final String PUBLISHER_URL = "publisher_url";
    }

    public static final String PATH_AUTHORS = "authors";
    public static final String PATH_AUTHOR = "authors/*";


    public static class Author implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_AUTHORS).build();
        public static final String TABLE = "author";
        public static final String FULL_NAME = "full_name";
    }

    public static class BookAuthor implements BaseColumns {
        public static final String TABLE = "book_author";
        public static final String BOOK_ID = "book_id";
        public static final String AUTHOR_ID = "author_id";
    }

    public static final String PATH_BOOKS_AUTHORS1 = "books_authors1";
    public static final Uri CONTENT_URI_BOOKS_AUTHORS1 =
            BASE_CONTENT_URI.buildUpon().appendPath(PATH_BOOKS_AUTHORS1).build();


    public static final String PATH_BOOKS_AUTHORS2 = "books_authors2";
    public static final Uri CONTENT_URI_BOOKS_AUTHORS2 =
            BASE_CONTENT_URI.buildUpon().appendPath(PATH_BOOKS_AUTHORS2).build();

    public static final String PATH_BOOKS_AUTHORS3 = "books_authors3";
    public static final Uri CONTENT_URI_BOOKS_AUTHORS3 =
            BASE_CONTENT_URI.buildUpon().appendPath(PATH_BOOKS_AUTHORS3).build();
}
