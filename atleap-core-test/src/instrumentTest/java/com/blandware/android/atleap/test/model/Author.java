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
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by agrebnev on 03.01.14.
 */
@DatabaseTable(tableName = TestContract.Author.TABLE)
public class Author {

    @DatabaseField(id = true, columnName = TestContract.Author._ID)
    public String userName;

    @DatabaseField(columnName = TestContract.Author.FULL_NAME)
    public String fullName;

    private Collection<Book> books;

    public Collection<Book> getBooks() {
        syncBooks();
        return books;
    }

    public void setBooks(Collection<Book> books) {
        this.books = books;
        syncBooks();
    }


    @ForeignCollectionField(eager = false)
    private Collection<BookAuthor> bookAuthors;

    public Collection<BookAuthor> getBookAuthors() {
        syncBooks();
        return bookAuthors;
    }

    public void setBookAuthors(Collection<BookAuthor> bookAuthors) {
        this.bookAuthors = bookAuthors;
        syncBooks();
    }

    private void syncBooks() {
        if (bookAuthors != null && this.books == null) {
            this.books = new ArrayList<Book>(bookAuthors.size());
            for (BookAuthor bookAuthor : bookAuthors) {
                this.books.add(bookAuthor.book);
            }
        } else if (bookAuthors == null && books != null) {
            this.bookAuthors = new ArrayList<BookAuthor>(books.size());
            for (Book book : books) {
                BookAuthor bookAuthor = new BookAuthor(book, this);
                this.bookAuthors.add(bookAuthor);
            }
        }
    }
}
