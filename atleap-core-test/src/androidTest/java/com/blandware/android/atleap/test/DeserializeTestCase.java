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

import android.test.AndroidTestCase;

import com.blandware.android.atleap.test.model.Book;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.io.IOUtils;

/**
 * Created by agrebnev on 03.01.14.
 */
public class DeserializeTestCase extends AndroidTestCase {
    private static final String BOOKS_FILENAME = "books_authors.json";


    public void testBooksAuthors() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = IOUtils.toString(getContext().getResources().getAssets().open(BOOKS_FILENAME));

        Book.BooksResult result = objectMapper.readValue(jsonString, Book.BooksResult.class);

        assertTrue(result.resultCode == 100);
        assertTrue(result.getBooks() != null);
        assertTrue(result.getBooks().size() == 2);

        assertTrue(result.getBooks().toArray(new Book[0])[0].getAuthors() != null);
        assertTrue(result.getBooks().toArray(new Book[0])[0].getAuthors().size() == 2);

        assertTrue(result.getBooks().toArray(new Book[0])[0].getTags() != null);
        assertTrue(result.getBooks().toArray(new Book[0])[0].getTags().size() == 2);

    }
}
