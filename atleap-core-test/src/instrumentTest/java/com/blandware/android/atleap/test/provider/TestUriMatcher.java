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

import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import com.blandware.android.atleap.provider.sqlite.SQLiteMatcherEntry;
import com.blandware.android.atleap.provider.ormlite.OrmLiteUriMatcher;
import com.blandware.android.atleap.test.model.Author;
import com.blandware.android.atleap.test.model.Book;
import com.blandware.android.atleap.test.model.BookAuthor;

import java.util.Locale;


/**
 * Created by agrebnev on 01.01.14.
 */
public class TestUriMatcher extends OrmLiteUriMatcher {

    private static final String TABLES_SQL_BOOKS_AUTHORS =
            TestContract.Book.TABLE
            + " INNER JOIN " + TestContract.BookAuthor.TABLE
            + " ON (" + TestContract.Book.TABLE + "." + TestContract.Book._ID + "="
            + TestContract.BookAuthor.TABLE + "." + TestContract.BookAuthor.BOOK_ID + ")"
            ;

    private static final String RAW_SQL_BOOKS_AUTHORS =
            "SELECT %s FROM " + TABLES_SQL_BOOKS_AUTHORS + " WHERE %s ORDER BY %s";

    public TestUriMatcher(String authority) {
        super(authority);
    }

    @Override
    public void instantiate() {

        addClass(TestContract.PATH_BOOKS, Book.class);
        addClass(TestContract.PATH_BOOK, Book.class);
        addClass(Book.BooksResult.class, TestContract.PATH_BOOKS);

        addClass(TestContract.PATH_AUTHORS, Author.class);
        addClass(TestContract.PATH_AUTHOR, Author.class);

        addClass(BookAuthor.class);


        addTablesSQL(TestContract.PATH_BOOKS_AUTHORS1, TABLES_SQL_BOOKS_AUTHORS);

        addRawSQL(TestContract.PATH_BOOKS_AUTHORS2, RAW_SQL_BOOKS_AUTHORS);

        addSQLBuilderCallback(TestContract.PATH_BOOKS_AUTHORS3, new SQLiteMatcherEntry.SQLBuilderCallback() {
            @Override
            public String getRawSQL(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
                StringBuilder projectionClause = new StringBuilder();
                SQLiteQueryBuilder.appendColumns(projectionClause, projection);
                String rawSQL = String.format(Locale.getDefault(), RAW_SQL_BOOKS_AUTHORS, projectionClause.toString(), selection, sortOrder);
                return rawSQL;
            }
        });
    }

}
