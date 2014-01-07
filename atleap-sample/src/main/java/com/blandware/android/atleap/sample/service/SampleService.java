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

package com.blandware.android.atleap.sample.service;

import android.app.Application;

import com.blandware.android.atleap.provider.ormlite.OrmLiteUriMatcher;
import com.blandware.android.atleap.sample.network.GitHub;
import com.blandware.android.atleap.sample.provider.SampleContract;
import com.blandware.android.atleap.sample.provider.SampleDatabaseHelper;
import com.blandware.android.atleap.sample.provider.SampleUriMatcher;
import com.octo.android.robospice.persistence.CacheManager;
import com.octo.android.robospice.persistence.exception.CacheCreationException;
import com.octo.android.robospice.persistence.ormlite.InDatabaseObjectPersisterFactory;
import com.octo.android.robospice.persistence.ormlite.RoboSpiceDatabaseHelper;
import com.octo.android.robospice.retrofit.RetrofitGsonSpiceService;

/**
 * Created by agrebnev on 22.12.13.
 */
public class SampleService extends RetrofitGsonSpiceService {

    private static final String BASE_URL = "https://api.github.com";


    @Override
    public void onCreate() {
        super.onCreate();
        addRetrofitInterface(GitHub.class);
    }

    @Override
    protected String getServerUrl() {
        return BASE_URL;
    }

    @Override
    public CacheManager createCacheManager(Application application) throws CacheCreationException {
        CacheManager cacheManager = new CacheManager();


        OrmLiteUriMatcher matcher = OrmLiteUriMatcher.getInstance(SampleUriMatcher.class, SampleContract.CONTENT_AUTHORITY);

        // init
        RoboSpiceDatabaseHelper databaseHelper = new RoboSpiceDatabaseHelper(application, SampleDatabaseHelper.DATABASE_NAME, SampleDatabaseHelper.DATABASE_VERSION);
        InDatabaseObjectPersisterFactory inDatabaseObjectPersisterFactory = new InDatabaseObjectPersisterFactory( application, databaseHelper, matcher.getClassToNotificationUriMap() );
        cacheManager.addPersister(inDatabaseObjectPersisterFactory);
        return cacheManager;
    }
}
