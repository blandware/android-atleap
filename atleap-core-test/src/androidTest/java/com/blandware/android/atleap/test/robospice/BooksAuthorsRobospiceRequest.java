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

package com.blandware.android.atleap.test.robospice;

import com.blandware.android.atleap.test.model.Book;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.octo.android.robospice.request.SpiceRequest;

import retrofit.RestAdapter;
import retrofit.converter.JacksonConverter;
import retrofit.http.GET;
import roboguice.util.temp.Ln;

/**
 * Created by agrebnev on 01.01.14.
 */

public class BooksAuthorsRobospiceRequest extends SpiceRequest<Book.BooksResult> implements SetUrlInterface {

    private BooksRetrofitService service;


    public BooksAuthorsRobospiceRequest() {
        super(Book.BooksResult.class);
    }

    @Override
    public void setUrl(String url) {
        ObjectMapper objectMapper = new ObjectMapper();
        //objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        //objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);

        RestAdapter restAdapter = new RestAdapter.Builder()
                //.setConverter(new GsonConverter(new Gson()))
                .setConverter(new JacksonConverter(objectMapper))
                .setEndpoint(url)
                .build();


        this.service = restAdapter.create(BooksRetrofitService.class);
    }

    @Override
    public Book.BooksResult loadDataFromNetwork() {
        Ln.d("Call web service ");
        //does not matter what params here because we use WebMockServer
        return service.getData();
    }

    public interface BooksRetrofitService {
        @GET("/test")
        Book.BooksResult getData();
    }
}
