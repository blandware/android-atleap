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
import com.blandware.android.atleap.util.StringUtil;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * Created by agrebnev on 01.01.14.
 */

@DatabaseTable(tableName = TestContract.Book.TABLE)
public class Book {

    @DatabaseField(id = true, columnName = TestContract.Book._ID)
    public int bookId;

    @DatabaseField(columnName = TestContract.Book.TITLE)
    public String title;

    @DatabaseField(columnName = TestContract.Book.PUBLISHING_DATE)
    public long publishingDate;

    /* --------------------- tags -------------------- */

    private Collection<String> tags;

    public Collection<String> getTags() {
        return tags;
    }

    public void setTags(Collection<String> tags) {
        this.tags = tags;
        this.tagsString = StringUtil.arrayToStr(this.tags.toArray(new String[0]));
    }

    @DatabaseField(columnName = TestContract.Book.TAGS_STRING, useGetSet = true)
    private String tagsString;

    public String getTagsString() {
        return tagsString;
    }

    public void setTagsString(String tagsString) {
        this.tagsString = tagsString;
        this.tags = Arrays.asList(StringUtil.strToArray(tagsString));
    }

    /* --------------------- authors -------------------- */
    private Collection<Author> authors;

    public Collection<Author> getAuthors() {
        syncAuthors();
        return authors;
    }

    public void setAuthors(Collection<Author> authors) {
        this.authors = authors;
        syncAuthors();
    }

    @ForeignCollectionField(eager = false)
    private Collection<BookAuthor> bookAuthors;

    public Collection<BookAuthor> getBookAuthors() {
        syncAuthors();
        return bookAuthors;
    }

    public void setBookAuthors(Collection<BookAuthor> bookAuthors) {
        this.bookAuthors = bookAuthors;
        syncAuthors();
    }

    private void syncAuthors() {
        if (authors == null && bookAuthors != null) {
            this.authors = new ArrayList<Author>(bookAuthors.size());
            for (BookAuthor bookAuthor : bookAuthors) {
                this.authors.add(bookAuthor.author);
            }
        } else if (authors != null && bookAuthors == null) {
            this.bookAuthors = new ArrayList<BookAuthor>(authors.size());
            for (Author author : authors) {
                BookAuthor bookAuthor = new BookAuthor(this, author);
                this.bookAuthors.add(bookAuthor);
            }
        }
    }

    /* --------------------- publisher -------------------- */

    public Publisher publisher;
    private static class Publisher {
        public String title;
        public String url;
    }

    @DatabaseField(columnName = TestContract.Book.PUBLISHER_TITLE, useGetSet = true)
    public String publisherTitle;

    @DatabaseField(columnName = TestContract.Book.PUBLISHER_URL, useGetSet = true)
    public String publisherUrl;

    public String getPublisherTitle() {
        if (publisherTitle == null) {
            publisherTitle = publisher.title;
        }
        return publisherTitle;
    }

    public void setPublisherTitle(String publisherTitle) {
        this.publisherTitle = publisherTitle;
    }

    public String getPublisherUrl() {
        if (publisherUrl == null) {
            publisherUrl = publisher.url;
        }
        return publisherUrl;
    }

    public void setPublisherUrl(String publisherUrl) {
        this.publisherUrl = publisherUrl;
    }



    @DatabaseField(foreign = true)
    public BooksResult result;


    @DatabaseTable(tableName = "book_result")
    public static class BooksResult {

        @DatabaseField(id = true)
        public int id = 0;

        public Integer resultCode;

        @ForeignCollectionField(eager = false)
        private Collection<Book> books;

        public Collection<Book> getBooks() {
            return this.books;
        }

        public void setBooks(Collection<Book> books) {
            this.books = books;
        }

    }
}