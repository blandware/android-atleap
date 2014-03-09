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

package com.blandware.android.atleap.test.model;

import com.blandware.android.atleap.test.provider.TestContract;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by agrebnev on 04.01.14.
 */
@DatabaseTable(tableName = TestContract.BookAuthor.TABLE)
public class BookAuthor {

    public BookAuthor() {

    }

    public BookAuthor(Book book, Author author) {
        this.book = book;
        this.author = author;
        initializeId();
    }


    @DatabaseField(id = true, useGetSet = true)
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        //do nothing
    }

    @DatabaseField(foreign = true, columnName = TestContract.BookAuthor.BOOK_ID, useGetSet = true)
    public Book book;

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
        initializeId();
    }

    @DatabaseField(foreign = true, columnName = TestContract.BookAuthor.AUTHOR_ID, useGetSet = true)
    public Author author;

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
        initializeId();
    }

    private void initializeId() {
        if (author != null && book != null && id == null) {
            id = book.bookId + "_" + author.userName;
        }
    }
}
